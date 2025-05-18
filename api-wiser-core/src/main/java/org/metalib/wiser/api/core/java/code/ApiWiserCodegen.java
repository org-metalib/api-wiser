package org.metalib.wiser.api.core.java.code;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.apache.commons.lang3.StringUtils;
import org.metalib.wiser.api.core.java.model.ApiWiserModel;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserSpecException;
import org.metalib.wiser.api.template.ApiWiserTemplates;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.SupportingFile;
import org.openapitools.codegen.languages.AbstractJavaCodegen;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULES;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_OPERATION;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_BUILD_DIR;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_DIR;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.ApiWiserFinals.SLASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_CONTEXT;
import static org.openapitools.codegen.utils.StringUtils.camelize;

public class ApiWiserCodegen extends AbstractJavaCodegen implements ApiWiserConfig {

    protected final Map<String, String> apiFolders = new HashMap<>();
    protected final Map<String, String> apiPackages = new HashMap<>();

    protected String baseEntityName;
    protected String buildDirectory = "target";
    protected String generatedSourceFolder = buildDirectory
            + File.separator + "generated-sources"
            + File.separator + "java";

    protected String generatedResourceFolder = buildDirectory
            + File.separator + "generated-resources"
            + File.separator + JAVA;

    public ApiWiserCodegen() {
        modelTemplateFiles.clear();
        ApiWiserTemplates.list()
                .stream()
                .filter(ApiWiserTemplateService::isModelFile)
                .forEach(v -> modelTemplateFiles.put(v.id(), v.fileExtension()));

        // "generateModelTests" -> true
        modelDocTemplateFiles.clear();  // "generateModelDocs" -> true

        apiTemplateFiles.clear();      // generateApis -> true
        ApiWiserTemplates.list()
                .stream()
                .filter(ApiWiserTemplateService::isApiFile)
                .forEach(v -> apiTemplateFiles.put(v.id(), v.fileExtension()));

        apiTestTemplateFiles.clear();   // "generateApiTests" -> true
        apiDocTemplateFiles.clear(); // generateApiDocs -> {Boolean@3952} true
    }

    @Override
    public String getName() {
        return API_WISER;
    }

    @Override
    public String defaultTemplatingEngine() {
        return null;
    }

    @Override
    public String modelFilename(String templateName, String modelName) {
        return ApiWiserTemplates.list()
                .stream()
                .filter(ApiWiserTemplateService::isModelFile)
                .filter(v -> templateName.equals(v.id()))
                .findFirst().map(v -> apiFilename(v, modelName))
                .orElseThrow();
    }

    @Override
    public String apiFilename(String templateName, String tag) {
        return ApiWiserTemplates.list()
                .stream()
                .filter(ApiWiserTemplateService::isApiFile)
                .filter(v -> templateName.equals(v.id()))
                .findFirst().map(v -> apiFilename(v, tag))
                .orElse((outputFolder
                        + File.separator + getArtifactId() + DASH + apiFolders.get(templateName)
                        + File.separator + sourceFolder
                        + File.separator + getInvokerPackage().replace('.', File.separatorChar)
                        + File.separator + apiPackages.get(templateName)).replace('/', File.separatorChar)
                        + File.separator + toApiFilename(tag) + apiTemplateFiles().get(templateName));
    }

    private String apiFilename(ApiWiserTemplateService s, String tag) {
        baseEntityName = tag;
        final var file = s.targetFile(this);
        return outputFolder
                + File.separator + file.relativeFolder()
                + File.separator + file.fileName() + "." + s.fileExtension();
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        updateApiWiserContext(objs);
        // adjust supporting files maven modules
        final var modules = Set.of(modules());
        supportingFiles.clear();
        supportingFiles.addAll(ApiWiserTemplates.list()
                .stream()
                .filter(ApiWiserTemplateService::isSupportingFile)
                .filter(v -> modules.isEmpty() || modules.contains(v.moduleName()))
                .map(this::toSupportingFile)
                .filter(Objects::nonNull)
                .collect(toList()));
        return objs;
    }

    private SupportingFile toSupportingFile(ApiWiserTemplateService templateService) {
        final var targetFile = templateService.targetFile(this);
        if (null == targetFile) {
            return null;
        }
        final var targetFileName = targetFile.fileName() + DOT + templateService.fileExtension();
        final var folder = targetFile.relativeFolder();
        return null == folder || folder.isBlank()
                ? new SupportingFile(templateService.id(), targetFileName)
                : new SupportingFile(templateService.id(), folder, targetFileName);
    }

    // ApiWiserConfig interface start
    @Override
    public String mavenGroupId() {
        return getGroupId();
    }

    @Override
    public String mavenArtifactId() {
        return getArtifactId();
    }

    @Override
    public String mavenVersion() {
        return getArtifactVersion();
    }

    @Override
    public String sourceFolder() {
        return getSourceFolder();
    }

    @Override
    public String generatedSourceFolder() {
        return generatedSourceFolder;
    }

    public ApiWiserCodegen generatedSourceFolder(String folder) {
        generatedSourceFolder = folder;
        return this;
    }

    @Override
    public String generatedResourceFolder() {
        return generatedResourceFolder;
    }

    public ApiWiserCodegen generatedResourceFolder(String folder) {
        generatedResourceFolder = folder;
        return this;
    }

    @Override
    public String basePackage() {
        return getInvokerPackage();
    }

    @Override
    public String baseEntityName() {
        return baseEntityName;
    }

    @Override
    public String camelizeBaseEntityName() {
        return camelize(baseEntityName());
    }

    @Override
    public String[] modules() {
        return Optional.ofNullable(additionalProperties()).map(v -> (List<String>) v.get(X_API_WISER_MODULES))
                .stream().flatMap(Collection::stream).toArray(String[]::new);
    }

    @Override
    public File projectDir() {
        return Optional.ofNullable(additionalProperties()).map(v -> (File) v.get(X_API_WISER_PROJECT_DIR)).orElse(null);
    }

    @Override
    public File projectBuildDir() {
        return Optional.ofNullable(additionalProperties()).map(v -> (File) v.get(X_API_WISER_PROJECT_BUILD_DIR)).orElse(null);
    }

    // ApiWiserConfig interfaces end

    @Override
    public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
        final String basePath = StringUtils.substringBefore(StringUtils.removeStart(resourcePath, "/"), "/");
        if (!StringUtils.isEmpty(basePath)) {
            co.subresourceOperation = !co.path.isEmpty();
        }
        co.baseName = basePath;
        if (StringUtils.isEmpty(co.baseName) || StringUtils.containsAny(co.baseName, "{", "}")) {
            co.baseName = "default";
        }
        final List<CodegenOperation> opList = operations.computeIfAbsent(co.baseName, k -> new ArrayList<>());
        opList.add(co);
    }

    // Default Codegen
    @Override
    public void setOpenAPI(OpenAPI openAPI) {
        if (openAPI == null) {
            throw new ApiWiserSpecException("openAPI is not specified");
        }
        super.setOpenAPI(openAPI);
    }

    @Override
    public Map<String, ModelsMap> postProcessAllModels(Map<String, ModelsMap> objs) {
        return objs;
    }

    @Override
    public ModelsMap postProcessModels(ModelsMap objs) {
        updateApiWiserContext(objs);
        return super.postProcessModels(objs);
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
        updateApiWiserContext(objs);
        Optional.of(objs)
                .map(OperationsMap::getOperations)
                .map(OperationMap::getPathPrefix)
                .flatMap(v -> Optional.of(openAPI)
                        .map(OpenAPI::getPaths)
                        .map(paths -> paths.get(SLASH + v)))
                .map(PathItem::getExtensions)
                .map(v -> (Map<String,Object>)v.get(X_API_WISER))
                .map(ApiWiserModel::apiWiserPath)
                .ifPresent(v -> objs.put(X_API_WISER_OPERATION, v));
        return super.postProcessOperationsWithModels(objs, allModels);
    }

    void updateApiWiserContext(Map<String, Object> objs) {
        Optional.of(openAPI)
                .map(OpenAPI::getExtensions)
                .map(v -> (Map<String, Object>) v.get(X_API_WISER))
                .map(ApiWiserModel::apiWiserContext)
                .ifPresent(v -> objs.put(X_API_WISER_CONTEXT, v));
    }
}