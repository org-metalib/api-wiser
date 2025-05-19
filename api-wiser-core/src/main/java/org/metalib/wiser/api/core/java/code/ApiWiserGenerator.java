package org.metalib.wiser.api.core.java.code;

import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserTemplates;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.Generator;
import org.openapitools.codegen.TemplateManager;
import org.openapitools.codegen.api.TemplatePathLocator;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.openapitools.codegen.templating.CommonTemplateContentLocator;
import org.openapitools.codegen.templating.GeneratorTemplateContentLocator;
import org.openapitools.codegen.templating.TemplateManagerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULES;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_TARGET_FILE;

/**
 * A code generator for the API Wiser system that extends the OpenAPI Tools DefaultGenerator.
 * This class is responsible for generating code from OpenAPI specifications based on the
 * configuration provided by {@link ApiWiserCodegen}.
 * 
 * The generator customizes the template processing to:
 * - Filter templates based on specified modules
 * - Customize the template processor to handle API Wiser specific requirements
 * - Provide access to the underlying configuration and bundle
 * 
 * It is typically created using the {@link ApiWiserCode#generator()} method.
 */
public class ApiWiserGenerator extends DefaultGenerator {

    public ApiWiserGenerator(boolean dryRun,
                             String packageName,
                             String apiPackageName,
                             String clientPackageName,
                             String serverPackageName,
                             String modelPackageName,
                             File outputDir,
                             File inputSpec,
                             String mavenGroupId,
                             String mavenArtifactId,
                             String mavenVersion,
                             Map<String, Object> additionalProperties) {
        super(dryRun);
        opts(new CodegenConfigurator()
                .setGeneratorName(ApiWiserCodegen.class.getCanonicalName())
                .setTemplatingEngineName(ApiWiserTemplatingEngine.ENGINE_ID)
                .setInstantiationTypes(Map.of())
                .setPackageName(packageName)
                .setApiPackage(apiPackageName)
                .setModelPackage(modelPackageName)
                .setOutputDir(outputDir.getAbsolutePath())
                .setInputSpec(inputSpec.getAbsolutePath())
                .setGroupId(mavenGroupId)
                .setArtifactId(mavenArtifactId)
                .setArtifactVersion(mavenVersion)
                .setAdditionalProperties(additionalProperties)
                .toClientOptInput());
    }
    @Override
    protected File processTemplateToFile(Map<String, Object> templateData,
                                         String templateName,
                                         String outputFilename,
                                         boolean shouldGenerate,
                                         String skippedByOption) throws IOException {
        final var modules = Optional.ofNullable(templateData).map(v -> (List<String>) v.get(X_API_WISER_MODULES))
                .stream().flatMap(Collection::stream).collect(Collectors.toSet());
        return ApiWiserTemplates.list()
                .stream()
                .filter(v -> templateName.equals(v.id()))
                .anyMatch(v -> modules.isEmpty() || modules.contains(v.moduleName()))
                ? super.processTemplateToFile(templateData, templateName, outputFilename, shouldGenerate, skippedByOption)
                : null;
    }

    @Override
    public Generator opts(ClientOptInput opts) {
        final var result = super.opts(opts);
        final var templateManagerOptions = new TemplateManagerOptions(this.config.isEnableMinimalUpdate(), this.config.isSkipOverwrite());
        final var commonTemplateLocator = new CommonTemplateContentLocator();
        final var generatorTemplateLocator = new GeneratorTemplateContentLocator(this.config);
        final var templateEngine = this.config.getTemplatingEngine();
        this.templateProcessor = new TemplateManager(templateManagerOptions, templateEngine,
                new TemplatePathLocator[]{generatorTemplateLocator, commonTemplateLocator}) {
            @Override
            public File write(Map<String, Object> data, String template, File target) throws IOException {
                data.put(X_API_WISER_TARGET_FILE, target);
                String templateContent = templateEngine.compileTemplate(this, data, template);
                return null == templateContent ? null : writeToFile(target.getPath(), templateContent);
            }
        };
        return result;
    }

    public ApiWiserCodegen config() {
        return (ApiWiserCodegen) config;
    }

    public ApiWiserBundle bundle() {
        return null;
    }


}
