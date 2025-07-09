package org.metalib.wiser.api.template.spring.webclient;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import org.metalib.wiser.api.javapoet.TypeRefInfo;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.springframework.web.reactive.function.client.WebClient;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static java.lang.String.format;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.ARRAY;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.ApiWiserFinals.QUERY_CAPITALISED;
import static org.metalib.wiser.api.template.spring.webclient.SpringWebClientTemplateBuilder.MODULE_NAME;
import static org.springframework.util.StringUtils.capitalize;

/**
 * Template service for generating a Jackson-based HTTP client.
 * 
 * <p>This template generates a Java HTTP client implementation that uses Jackson for JSON
 * serialization/deserialization. The generated client uses Java's HttpClient to make HTTP
 * requests and handles different HTTP methods (GET, POST, PUT, PATCH, DELETE).</p>
 * 
 * <p>The client processes path parameters, query parameters, and request bodies, and handles
 * response deserialization using Jackson. It also includes error handling for HTTP status
 * codes and exceptions.</p>
 */
public class SpringWebClientTemplate implements ApiWiserTemplateService {

    static final String QUERY = "query";

    /** The name of this template */
    public static final String TEMPLATE_NAME = "spring-webclient";

    /** The unique identifier for this template */
    public static final String TEMPLATE_ID = API_WISER + DOUBLE_COLON + TEMPLATE_NAME;

    /**
     * Returns the unique identifier for this template.
     * 
     * @return the template ID
     */
    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    /**
     * Returns the name of the module this template belongs to.
     * 
     * @return the module name
     */
    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    /**
     * Indicates that this template generates an API file rather than a supporting file.
     * 
     * @return true as this is an API template
     */
    @Override
    public boolean isApiFile() {
        return true;
    }

    /**
     * Returns the file extension for the generated file.
     * 
     * @return "java" as the file extension
     */
    @Override
    public String fileExtension() {
        return JAVA;
    }

    /**
     * Defines the target file location for the generated HTTP client class.
     * 
     * @param config the API Wiser configuration
     * @return the target file information
     */
    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId()
                        + DASH
                        + MODULE_NAME
                        + File.separator + config.generatedSourceFolder()
                        + File.separator + config.basePackage().replace(DOT, File.separator)
                        + File.separator + MODULE_NAME.replace(DASH, File.separator);
            }

            @Override
            public String fileName() {
                return config.camelizeBaseEntityName() + LOWER_HYPHEN.to(UPPER_CAMEL, TEMPLATE_NAME);
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        return JavaFile.builder(bundle.basePackage() + DOT + TEMPLATE_NAME.replace(DASH, DOT),
                httpClientTypeBuilder(bundle).build())
                .skipJavaLangImports(true)
                .build().toString();
    }

    static TypeSpec.Builder httpClientTypeBuilder(ApiWiserBundle bundle) {
        final var operations = bundle.codeOperation();
        final var className = bundle.camelizeBaseName() + LOWER_HYPHEN.to(UPPER_CAMEL, TEMPLATE_NAME);
        final var imports = bundle.imports();
        final var commonPath = bundle.commonPath();
        final var classBuilder = TypeSpec
                .classBuilder(className)
                .addAnnotation(RequiredArgsConstructor.class)
                .addSuperinterface(ClassName.get(bundle.basePackage() + DOT + API,
                        bundle.camelizeBaseName() + "Api"))
                .addModifiers(Modifier.PUBLIC)
                .addField(WebClient.class, "client$", Modifier.FINAL);
        operations
                .map(ApiWiserBundle.CodeOperation::operations)
                .stream()
                .flatMap(Collection::stream)
                .forEach(o -> classBuilder.addMethod(apiMethodSpec(o, commonPath, imports)));
        return classBuilder;
    }

    private static MethodSpec apiMethodSpec(ApiWiserBundle.CodeOperation.Op operation, String commonPath, Set<String> imports) {
        final var queryParamMaxInLine = operation.queryParamMaxInLine();
        final var extendedImports = new HashSet<>(imports);
        final var operationId = operation.operationId();
        final var operationOpt = Optional.of(operation);
        final var returnType = operation.returnType();
        if (operationOpt
                .map(ApiWiserBundle.CodeOperation.Op::returnProperty)
                .filter(v -> "array".equals(v.containerType()) && v.dataType().startsWith("List<"))
                .isPresent() ||
                operationOpt
                        .stream()
                        .flatMap(v -> v.allParams().stream())
                        .map(v -> v.dataType().startsWith("List<"))
                        .findFirst().isPresent()) {
            extendedImports.add("java.util.List");
        }
        if (operationOpt
                .map(ApiWiserBundle.CodeOperation.Op::returnProperty)
                .filter(v -> "map".equals(v.containerType()) && v.dataType().startsWith("Map<"))
                .isPresent() ||
                operationOpt
                        .stream()
                        .map(ApiWiserBundle.CodeOperation.Op::allParams)
                        .flatMap(Collection::stream)
                        .anyMatch(v -> v.dataType().startsWith("Map<"))) {
            extendedImports.add("java.util.Map");
        }
        final var method = MethodSpec
                .methodBuilder(operationId)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeRefInfo.parse(returnType, extendedImports).toTypeName());

        // First, always add all body params, usually just one.
        Optional.of(operation)
                .map(ApiWiserBundle.CodeOperation.Op::bodyParams)
                .stream()
                .flatMap(Collection::stream)
                .forEach(p -> method.addParameter(factualParameter(p, extendedImports)));

        // Second, we respect the path params
        Optional.of(operation)
                .map(ApiWiserBundle.CodeOperation.Op::pathParams)
                .stream()
                .flatMap(Collection::stream)
                .forEach(p -> method.addParameter(factualParameter(p, extendedImports)));

        // Finally, we add the query parameters! Since there may be several, let’s organize them into a single class for better management.
        final var queryParams = Optional.of(operation).map(ApiWiserBundle.CodeOperation.Op::queryParams);
        queryParams.ifPresent(qParams -> {
            if (qParams.size() < queryParamMaxInLine) {
                qParams.forEach(p -> method.addParameter(factualParameter(p, extendedImports)));
            } else {
                method.addParameter(ParameterSpec.builder(TypeRefInfo.parse(capitalize(operationId) + QUERY_CAPITALISED, imports).toTypeName(), "query").build());
            }
        });

        return method.addCode(methodBody(operationId, operationOpt)).build();
    }

    /**
     * Creates a parameter specification for an API operation parameter.
     *
     * <p>This method generates the Java code for a method parameter in the HTTP client.</p>
     *
     * @param parameter the API parameter to generate a specification for
     * @param extendedImports the set of imports to be extended with any additional imports needed
     * @return the parameter specification
     */
    static ParameterSpec factualParameter(ApiWiserBundle.CodeParameter parameter, Set<String> extendedImports) {
        return ParameterSpec
                .builder(TypeRefInfo.parse(parameter.dataType(), extendedImports).toTypeName(), parameter.name())
                .build();
    }

    static CodeBlock methodBody(String operationId, Optional<ApiWiserBundle.CodeOperation.Op> operationOpt) {
        final var httpMethod = operationOpt.map(ApiWiserBundle.CodeOperation.Op::httpMethod).orElse("NOT_SPECIFIED");
        final var result = CodeBlock.builder();

        final var path = operationOpt.map(ApiWiserBundle.CodeOperation.Op::path).orElse("");

        final var pathParams = operationOpt
                .stream()
                .map(ApiWiserBundle.CodeOperation.Op::pathParams)
                .flatMap(Collection::stream)
                .toList();
        if (!pathParams.isEmpty()) {
            final var pathParamsCode = CodeBlock.builder().addStatement("final var pathVars = new $T<$T,$T>()",
                    HashMap.class, String.class, Object.class);
            pathParams.forEach(p -> pathParamsCode.addStatement("pathVars.put($S, $L)", p.name(), p.name()));
            result.add(pathParamsCode.build());
        }

        operationOpt.map(ApiWiserBundle.CodeOperation.Op::returnType).ifPresent(returnType -> result.add("return "));
        result.add("client$$.$L()\n", httpMethod.toLowerCase());

        final var queryParamMaxInLine = operationOpt.get().queryParamMaxInLine();
        final var queryParams = operationOpt
                .stream()
                .map(ApiWiserBundle.CodeOperation.Op::queryParams)
                .flatMap(Collection::stream)
                .toList();

        if (pathParams.isEmpty() && queryParams.isEmpty()) {
            result.indent().add(".uri($S)", path);
        } else {
            final var uriBuilder = CodeBlock.builder();
            uriBuilder.indent().add("uri.path($S)\n", path);

            if (queryParams.size() < queryParamMaxInLine) {
                queryParams.forEach(p -> uriBuilder.add(".queryParam($S, $L)\n", p.baseName(), p.name()));
            } else {
                final var queryParameterClass = capitalize(operationId) + QUERY_CAPITALISED;
                queryParams.forEach(p -> uriBuilder.add(".queryParamIfPresent($S, $T.of($L).map($L::$L))\n",
                        p.baseName(), Optional.class, QUERY, queryParameterClass, "get" + capitalize(p.name())));
            }

            result.indent().add(format(".uri(uri -> $L.build(%s))", pathParams.isEmpty() ? "" : "pathVars"),
                    uriBuilder.unindent().build());
        }

        operationOpt.stream().map(ApiWiserBundle.CodeOperation.Op::allParams).flatMap(Collection::stream)
                .filter(ApiWiserBundle.CodeParameter::isBodyParam).findFirst().ifPresent(p -> {
                    result.add("\n.bodyValue($L)", p.name());
                });

        result.add("\n.retrieve()");

        operationOpt.map(ApiWiserBundle.CodeOperation.Op::returnProperty).ifPresentOrElse(p -> {
            if (ARRAY.equals(p.containerType())) {
                result.add("\n.bodyToFlux($L.class)\n.collectList()\n.block()", p.complexType());
            } else {
                result.add("\n.bodyToMono($L.class)\n.block()", p.baseType());
            }
        }, () -> result.add("\n.toBodilessEntity()\n.block()"));

        result.add(";");

        return result.unindent().build();
    }
}
