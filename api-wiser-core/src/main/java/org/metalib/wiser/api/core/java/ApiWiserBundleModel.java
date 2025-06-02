package org.metalib.wiser.api.core.java;

import lombok.RequiredArgsConstructor;
import org.metalib.wiser.api.core.java.model.ApiWiserContext;
import org.metalib.wiser.api.core.java.model.ApiWiserPath;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.MavenDependency;
import org.metalib.wiser.api.template.model.ApiWiserBean;
import org.metalib.wiser.api.template.model.ApiWiserBeanContext;
import org.openapitools.codegen.CodegenModel;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenProperty;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.OperationMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.MAVEN_COORDINATES;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_DEPENDENCIES_NAME;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULE;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULES;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_OPERATION;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_BUILD_DIR;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_DIR;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_API_PACKAGE;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_BASE_ENTITY_NAME;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_CONTEXT;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_GENERATED_RESOURCE_FOLDER;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_GENERATED_SOURCE_FOLDER;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_SOURCE_FOLDER;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_TARGET_FILE;
import static org.openapitools.codegen.utils.StringUtils.camelize;

@RequiredArgsConstructor
public class ApiWiserBundleModel implements ApiWiserBundle {

    static final String BUNDLE_OPERATIONS_API_INTERFACE = "lombok-operations-parent-api-interface";
    static final String BUNDLE_OPERATIONS_BIZ_CLASS = "lombok-operations-parent-biz-class";
    static final String PACKAGE = "package";
    static final String PACKAGE_NAME = "packageName";

    final Optional<Map<String, Object>> bundleOpt;

    public static ApiWiserBundleModel wrap(Map<String, Object> bundle) {
        return new ApiWiserBundleModel(Optional.of(bundle));
    }

    public Optional<Map<String, Object>> opt() {
        return bundleOpt;
    }

    public ApiWiserBundleModel bundlePackage(final String packageName) {
        bundleOpt.ifPresent(v -> v.put(PACKAGE, packageName));
        return this;
    }

    @Override
    public String basePackage() {
        return bundleOpt.map(v -> v.get(PACKAGE_NAME))
                .map(Object::toString)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public String bundlePackage() {
        return bundleOpt.map(v -> v.get(PACKAGE)).map(Object::toString).orElseThrow();
    }

    @Override
    public String baseName() {
        return operation().map(v -> v.baseName).orElseThrow(NullPointerException::new);
    }

    @Override
    public String camelizeBaseName() {
        return camelize(baseName());
    }

    @Override
    public CodeType apiInterfaceName() {
        return operations()
                .map(v -> (CodeType) v.get(BUNDLE_OPERATIONS_API_INTERFACE))
                .orElseThrow();
    }

    public ApiWiserBundleModel apiInterfaceName(final String packageName, final String className) {
        operations()
                .ifPresent(
                        v -> v.put(BUNDLE_OPERATIONS_API_INTERFACE, new CodeType() {
                            @Override
                            public String packageName() {
                                return packageName;
                            }

                            @Override
                            public String className() {
                                return className;
                            }
                        }));
        return this;
    }

    @Override
    public CodeType bizClassName() {
        return operations()
                .map(v -> (CodeType) v.get(BUNDLE_OPERATIONS_BIZ_CLASS))
                .orElseThrow();
    }

    public ApiWiserBundleModel bizClassName(String packageName, String className) {
        operations()
                .ifPresent(
                        v -> v.put(BUNDLE_OPERATIONS_BIZ_CLASS, new CodeType() {
                            @Override
                            public String packageName() {
                                return packageName;
                            }

                            @Override
                            public String className() {
                                return className;
                            }
                        }));
        return this;
    }

    @Override
    public String className() {
        return operations()
                .map(OperationMap::getClassname)
                .orElseGet(() -> model()
                        .map(CodegenModel::getClassname)
                        .orElseThrow());
    }

    public ApiWiserBundleModel className(final String className) {
        operations()
                .ifPresentOrElse(
                        v -> v.setClassname(className),
                        () -> model()
                                .ifPresent(v -> v.setClassname(className)));
        return this;
    }

    @Override
    public Map<String, Object> unwrap() {
        return bundleOpt.orElseThrow();
    }

    @Override
    public String groupId() {
        return bundleOpt.map(v -> (String) v.get("groupId")).orElseThrow();
    }

    @Override
    public String artifactId() {
        return bundleOpt.map(v -> (String) v.get("artifactId")).orElseThrow();
    }

    @Override
    public String artifactVersion() {
        return bundleOpt.map(v -> (String) v.get("artifactVersion")).orElseThrow();
    }

    @Override
    public Map<String, String> mavenDependencyVersions() {
        return bundleOpt.map(v -> (List<String>) v.get(X_API_WISER_MAVEN_DEPENDENCIES_NAME))
                .stream()
                .flatMap(Collection::stream)
                .collect(toMap(ApiWiserBundleModel::extractMavenCoord, ApiWiserBundleModel::extractMavenVersion));
    }

    @Override
    public ApiWiserBeanContext beanContext() {
        final var result = bundleOpt
                .map(v -> (ApiWiserPath) v.get(X_API_WISER_OPERATION))
                .map(ApiWiserPath::getContext)
                .map(v -> v.get(module()))
                .map(context -> new ApiWiserBeanContext() {
                    @Override
                    public List<String> imports() {
                        return Optional.of(context)
                                .map(ApiWiserPath.ModuleContext::getImports)
                                .orElse(List.of());
                    }
                    @Override
                    public List<ApiWiserBean> beans() {
                        return Optional.of(context)
                                .map(ApiWiserPath.ModuleContext::getProperties)
                                .stream()
                                .flatMap(Collection::stream)
                                .map(vv -> new ApiWiserBean() {
                                    @Override
                                    public String name() {
                                        return vv.getName();
                                    }
                                    @Override
                                    public String type() {
                                        return vv.getType();
                                    }
                                }).collect(toList());
                    }
                }).orElse(null);
        return null == result ? ApiWiserBeanContext.EMPTY : result;
    }

    @Override
    public Set<String> modules() {
        return bundleOpt.map(v -> (List<String>) v.get(X_API_WISER_MODULES)).stream()
                .flatMap(Collection::stream).collect(toSet());
    }

    @Override
    public File projectDir() {
        return bundleOpt.map(v -> (File) v.get(X_API_WISER_PROJECT_DIR)).orElse(null);
    }

    @Override
    public File targetFile() {
        return bundleOpt.map(v -> (File) v.get(X_API_WISER_TARGET_FILE)).orElse(null);
    }

    @Override
    public String sourceFolder() {
        return bundleOpt.map(v -> v.get(X_API_WISER_SOURCE_FOLDER).toString()).orElse(null);
    }

    @Override
    public String generatedSourceFolder() {
        return bundleOpt.map(v -> v.get(X_API_WISER_GENERATED_SOURCE_FOLDER).toString()).orElse(null);
    }

    @Override
    public String generatedResourceFolder() {
        return bundleOpt.map(v -> v.get(X_API_WISER_GENERATED_RESOURCE_FOLDER).toString()).orElse(null);
    }

    @Override
    public String apiPackage() {
        return bundleOpt.map(v -> v.get(X_API_WISER_API_PACKAGE).toString()).orElse(null);
    }

    @Override
    public String baseEntityName() {
        return bundleOpt.map(v -> v.get(X_API_WISER_BASE_ENTITY_NAME).toString()).orElse(null);
    }


    @Override
    public String module() {
        return bundleOpt.map(v -> (String) v.get(X_API_WISER_MODULE)).orElse("");
    }

    public List<MavenDependency<?>> moduleDependencies() {
        return bundleOpt.map(v -> (ApiWiserContext) v.get(X_API_WISER_CONTEXT))
                .map(v -> v.getDependencies())
                .map(v -> v.get(module()))
                .map(v -> new ArrayList(v))
                .orElse(new ArrayList<>());
    }


    @Override
    public Map<String, Object> extraProperties() {
        return bundleOpt.map(Map::entrySet)
                .stream()
                .flatMap(Collection::stream)
                .filter(v -> v.getKey().startsWith(X_API_WISER))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public File projectBuildDir() {
        return bundleOpt.map(v -> (File) v.get(X_API_WISER_PROJECT_BUILD_DIR)).orElse(null);
    }

    static String extractMavenCoord(String dependency) {
        final var coords = dependency.split(":");
        if (0 == coords.length) {
            throw new MavenCoordParsingException(format("Group Id is not specified in <%s>", dependency));
        }
        final var result = new StringBuilder(coords[0].trim());
        if (1 == coords.length) {
            throw new MavenCoordParsingException(format("Artifact Id is not specified in <%s>", dependency));
        }
        return result.append(":").append(coords[1].trim()).toString();
    }

    static String extractMavenVersion(String dependency) {
        final var coords = dependency.split(":");
        if (coords.length < 3) {
            throw new MavenCoordParsingException(format("Version is not specified in <%s>", dependency));
        }
        return coords[2].trim();
    }

    public static final class MavenCoordParsingException extends RuntimeException {
        MavenCoordParsingException(String message) {
            super(message);
        }
    }

    @Override
    public Optional<CodeModel> codeModel() {
        return model().map(ApiWiserBundleModel::toCodeModel);
    }

    static CodeModel toCodeModel(CodegenModel source) {
        if (null == source) {
            return null;
        }
        return new CodeModel() {
            @Override
            public boolean isEnum() {
                return source.isEnum;
            }

            @Override
            public String name() {
                return source.getName();
            }

            @Override
            public String description() {
                return source.getDescription();
            }

            @Override
            public CodeDiscriminator discriminator() {
                return Optional.of(source)
                        .map(CodegenModel::getDiscriminator)
                        .map(v -> new CodeDiscriminator() {
                            @Override
                            public String propertyName() {
                                return v.getPropertyName();
                            }
                        })
                        .orElse(null);
            }

            @Override
            public String dataType() {
                return source.getDataType();
            }

            @Override
            public List<EnumVar> enumVars() {
                return ((List<HashMap<String, Object>>) source.getAllowableValues().get("enumVars")).stream()
                        .map(enumVar -> new EnumVar() {
                            @Override
                            public String name() {
                                return enumVar.get("name").toString();
                            }

                            @Override
                            public Object value() {
                                return enumVar.get("value");
                            }
                        }).collect(toList());
            }

            @Override
            public List<CodeProperty> vars() {
                return source.getVars().stream()
                        .map(ApiWiserBundleModel::toCodeProperty)
                        .toList();
            }

            @Override
            public CodeModel parentModel() {
                return toCodeModel(source.getParentModel());
            }

            @Override
            public List<CodeModel> children() {
                return Optional.of(source)
                        .map(CodegenModel::getChildren)
                        .stream()
                        .flatMap(Collection::stream)
                        .map(ApiWiserBundleModel::toCodeModel)
                        .toList();
            }
        };
    }
    static CodeProperty toCodeProperty(CodegenProperty property) {
        if (null == property) {
            return null;
        }
        return new CodeProperty() {

            @Override
            public String containerType() {
                return property.getContainerType();
            }

            @Override
            public String baseType() {
                return property.getBaseType();
            }

            @Override
            public String complexType() {
                return property.getComplexType();
            }

            @Override
            public String dataType() {
                return property.getDataType();
            }

            @Override
            public String datatypeWithEnum() {
                return property.getDatatypeWithEnum();
            }

            @Override
            public String baseName() {
                return property.getBaseName();
            }

            @Override
            public String enumName() {
                return property.getEnumName();
            }

            @Override
            public List<EnumVar> enumVars() {
                return ((List<HashMap<String, Object>>) property.getAllowableValues().get("enumVars")).stream()
                        .map(enumVar -> new EnumVar() {
                            @Override
                            public String name() {
                                return enumVar.get("name").toString();
                            }

                            @Override
                            public Object value() {
                                return enumVar.get("value");
                            }
                        }).collect(toList());
            }

            @Override
            public String name() {
                return property.getName();
            }

            @Override
            public String defaultValue() {
                return property.getDefaultValue();
            }

            @Override
            public String description() {
                return property.getDescription();
            }

            @Override
            public CodeProperty items() {
                return toCodeProperty(property.getItems());
            }

            @Override
            public boolean required() {
                return property.getRequired();
            }

            @Override
            public boolean isInnerEnum() {
                return property.isInnerEnum;
            }
        };
    }

    public Optional<CodegenModel> model() {
        return modelMaps().stream()
                .flatMap(Collection::stream)
                .findFirst()
                .map(v -> (CodegenModel) v.get("model"));
    }

    @Override
    public Optional<CodeOperation> codeOperation() {
        return operations().map(v -> new CodeOperation() {
            @Override
            public String className() {
                return camelize(v.getClassname());
            }

            @Override
            public List<Op> operations() {
                final var operations = v.getOperation().stream().map(vv -> new Op() {
                    @Override
                    public String operationId() {
                        return vv.operationId;
                    }

                    @Override
                    public String baseName() {
                        return vv.baseName;
                    }
                    @Override
                    public String returnType() {
                        return vv.returnType;
                    }

                    @Override
                    public CodeProperty returnProperty() {
                        return toCodeProperty(vv.returnProperty);
                    }

                    @Override
                    public String httpMethod() {
                        return vv.httpMethod;
                    }

                    @Override
                    public String path() {
                        return vv.path;
                    }

                    @Override
                    public List<MediaType> consumes() {
                        return Optional.of(vv)
                                .map(vvv -> vvv.consumes)
                                .stream()
                                .flatMap(Collection::stream)
                                .map(vvv -> (MediaType) () -> vvv.get("mediaType")).toList();
                    }

                    @Override
                    public List<MediaType> produces() {
                        return Optional.of(vv)
                                .map(vvv -> vvv.produces)
                                .stream()
                                .flatMap(Collection::stream)
                                .map(vvv -> (MediaType) () -> vvv.get("mediaType")).toList();
                    }

                    @Override
                    public List<CodeParameter> allParams() {
                        return vv.allParams.stream().map(vvv -> new CodeParameter() {
                            @Override
                            public String dataType() {
                                return vvv.dataType;
                            }

                            @Override
                            public String baseName() {
                                return vvv.baseName;
                            }

                            @Override
                            public String name() {
                                return vvv.paramName;
                            }

                            @Override
                            public boolean isPathParam() {
                                return vvv.isPathParam;
                            }

                            @Override
                            public boolean isQueryParam() {
                                return vvv.isQueryParam;
                            }

                            @Override
                            public boolean isBodyParam() {
                                return vvv.isBodyParam;
                            }
                        }).collect(toList());
                    }

                    @Override
                    public List<CodeResponse> responses() {
                        return vv.responses.stream().map(vvv -> new CodeResponse() {

                            @Override
                            public String code() {
                                return vvv.code;
                            }

                            @Override
                            public boolean is1xx() {
                                return vvv.is1xx;
                            }

                            @Override
                            public boolean is2xx() {
                                return vvv.is2xx;
                            }

                            @Override
                            public boolean is3xx() {
                                return vvv.is3xx;
                            }

                            @Override
                            public boolean is4xx() {
                                return vvv.is4xx;
                            }

                            @Override
                            public boolean is5xx() {
                                return vvv.is5xx;
                            }
                        }).collect(toList());
                    }
                }).toList();
                final var m = new HashMap<String, List<Op>>();
                operations.forEach(o -> m.computeIfAbsent(o.operationId(), k -> new ArrayList<>()).add(o));
                return m.keySet().stream().flatMap(k -> {
                    final var ops = m.get(k);
                    if (1 == ops.size()) {
                        return ops.stream();
                    }
                    final var opsMap = new HashMap<String,Op>();
                    ops.forEach(op -> opsMap.computeIfAbsent(
                            op.allParams().stream().map(CodeParameter::name).collect(joining("|")),
                            kk -> op));
                    return opsMap.values().stream();
                }).toList();
            }
        });
    }

    public Optional<OperationMap> operations() {
        return bundleOpt.map(v -> (OperationMap) v.get("operations"));
    }

    private Optional<CodegenOperation> operation() {
        return operations()
                .map(OperationMap::getOperation)
                .stream()
                .flatMap(Collection::stream).findFirst();
    }

    @Override
    public String pathPrefix() {
        return "/" + operations().map(OperationMap::getPathPrefix).orElse("");
    }

    @Override
    public String commonPath() {
        var path = (String) null;
        for (final var p : operations()
                .map(OperationMap::getOperation)
                .stream()
                .flatMap(Collection::stream)
                .map(v -> v.path)
                .collect(toSet())) {
            if (null == path) {
                path = p;
            } else {
                path = commonPathPrefix(path, p);
            }
        }
        return path;
    }

    static String commonPathPrefix(String s1, String s2) {
        final var p1 = s1.split("/");
        final var p2 = s2.split("/");
        return p1.length < p2.length ? commonPathPrefix(p1, p2) : commonPathPrefix(p2, p1);
    }

    static String commonPathPrefix(String[] p1, String[] p2) {
        final var result = new StringBuilder();
        for (int i = 0; i < p1.length; i++) {
            final var s = p1[i];
            if (s.isBlank()) {
                continue;
            }
            if (s.equals(p2[i])) {
                result.append("/").append(s);
            } else {
                break;
            }
        }
        return result.toString();
    }

    private Optional<List<ModelMap>> modelMaps() {
        return bundleOpt
                .map(v -> (List<ModelMap>) v.get("models"));
    }

    @Override
    public Set<String> imports() {
        return bundleOpt
                .map(v -> (List<Map<String, String>>) v.get("imports"))
                .stream()
                .flatMap(Collection::stream)
                .map(v -> v.get("import"))
                .collect(toSet());
    }

    public Map<MavenDependency, String> mavenCoordinates() {
        return bundleOpt
                .map(v -> (Map<MavenDependency, String>) v.get(MAVEN_COORDINATES))
                .orElseGet(Map::of);
    }
}
