package org.metalib.wiser.api.template.jackson.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeSpec;
import lombok.RequiredArgsConstructor;
import org.metalib.net.url.jersey.JerseyUriBuilder;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.javapoet.TypeRefInfo;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.CLIENT;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.HTTP;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.MODULE_NAME;

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
public class JacksonHttpClientTemplate implements ApiWiserTemplateService {

    /** The name of this template */
    public static final String TEMPLATE_NAME = "jackson-http-client";

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
                        + File.separator + HTTP
                        + File.separator + CLIENT;
            }

            @Override
            public String fileName() {
                return config.camelizeBaseEntityName() + LOWER_HYPHEN.to(UPPER_CAMEL, HTTP + DASH + CLIENT);
            }
        };
    }

    /**
     * Generates the Java code for the HTTP client class.
     * 
     * @param bundle the API Wiser bundle containing configuration and context
     * @return the generated Java code
     */
    @Override
    public String toText(ApiWiserBundle bundle) {
        final var httpClientPackage = bundle.basePackage() + DOT + HTTP + DOT + CLIENT;
        return JavaFile.builder(httpClientPackage, httpClientTypeBuilder(bundle).build())
                .build().toString();
    }

    /**
     * Builds the TypeSpec for the HTTP client class.
     * 
     * <p>This method creates a Java class that implements the API interface and provides
     * HTTP client implementations for all API operations. The client uses Java's HttpClient
     * and Jackson for JSON processing.</p>
     * 
     * @param bundle the API Wiser bundle containing configuration and context
     * @return the TypeSpec builder for the HTTP client class
     */
    static TypeSpec.Builder httpClientTypeBuilder(ApiWiserBundle bundle) {
        final var operations = bundle.codeOperation();
        final var className = bundle.camelizeBaseName() + LOWER_HYPHEN.to(UPPER_CAMEL, HTTP + DASH + CLIENT);
        final var imports = bundle.imports();
        final var commonPath = bundle.commonPath();
        final var preResponseHandlerType = ParameterizedTypeName.get(BiFunction.class, String.class, HttpResponse.ResponseInfo.class,
                Boolean.class);
        final var classBuilder = TypeSpec
                .classBuilder(className)
                .addAnnotation(RequiredArgsConstructor.class)
                .addSuperinterface(ClassName.get(bundle.basePackage() + DOT + API,
                        bundle.camelizeBaseName() + "Api"))
                .addModifiers(Modifier.PUBLIC)
                .addField(String.class, "baseUrl$", Modifier.FINAL)
                .addField(HttpClient.class, "httpClient$", Modifier.FINAL)
                .addField(ObjectMapper.class, "jackson$", Modifier.FINAL)
                .addField(preResponseHandlerType, "preResponseHandler$")
                .addMethod(MethodSpec.methodBuilder("preResponseHandler")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(preResponseHandlerType, "preResponseHandler")
                        .addCode(CodeBlock.builder()
                                .addStatement("preResponseHandler$$ = preResponseHandler")
                                .addStatement("return this")
                                .build())
                        .returns(ClassName.bestGuess(className))
                        .build())
                ;
        operations
                .map(ApiWiserBundle.CodeOperation::operations)
                .stream()
                .flatMap(Collection::stream)
                .forEach(o -> classBuilder.addMethod(apiMethodSpec(o, commonPath, imports)));
        return classBuilder;
    }

    /**
     * Creates a method specification for an API operation.
     * 
     * <p>This method generates the Java code for a single API operation method in the HTTP client.
     * It handles the method parameters, return type, and imports required for the operation.</p>
     * 
     * @param operation the API operation to generate a method for
     * @param commonPath the common base path for all API operations
     * @param imports the set of imports to be extended with any additional imports needed
     * @return the method specification for the API operation
     */
    private static MethodSpec apiMethodSpec(ApiWiserBundle.CodeOperation.Op operation, String commonPath, Set<String> imports) {
        final var extendedImports = new HashSet<>(imports);
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
                .methodBuilder(operation.operationId())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeRefInfo.parse(returnType, extendedImports).toTypeName());
        Optional.of(operation)
                .map(ApiWiserBundle.CodeOperation.Op::allParams)
                .stream()
                .flatMap(Collection::stream)
                .forEach(p -> method.addParameter(factualParameter(p, extendedImports)));
        return method
                .addCode(methodBody(commonPath, operationOpt))
                .build();
    }

    /**
     * Generates the method body for an API operation.
     * 
     * <p>This method creates the code block that implements the HTTP request logic for an API operation.
     * It handles URL building, query parameters, path parameters, request body, and HTTP method.</p>
     * 
     * @param commonPath the common base path for all API operations
     * @param operationOpt the optional API operation to generate a method body for
     * @return the code block for the method body
     */
    static CodeBlock methodBody(String commonPath, Optional<ApiWiserBundle.CodeOperation.Op> operationOpt) {
        final var result = CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("final var url$$ = new $T().uri(baseUrl$$).path($S)",
                        JerseyUriBuilder.class,
                        operationOpt.map(ApiWiserBundle.CodeOperation.Op::path)
                                .filter(v -> !v.isBlank()).orElse(commonPath));
        operationOpt
                .stream()
                .map(ApiWiserBundle.CodeOperation.Op::allParams)
                .flatMap(Collection::stream)
                .filter(ApiWiserBundle.CodeParameter::isQueryParam)
                .forEach(p -> result
                        .addStatement("$T.ofNullable($L).ifPresent(v -> url$$.queryParam($S, $L))",
                                Optional.class, p.name(), p.baseName(), p.name()));
        final var pathParams = operationOpt
                .stream()
                .map(ApiWiserBundle.CodeOperation.Op::allParams)
                .flatMap(Collection::stream)
                .filter(ApiWiserBundle.CodeParameter::isPathParam)
                .toList();
        String urlBuildExpr;
        if (pathParams.isEmpty()) {
            urlBuildExpr = ".build()";
        } else {
            result.addStatement("final var pathParams$$ = new $T<$T,$T>()", HashMap.class, String.class, Object.class);
            pathParams.forEach(p -> result.addStatement("pathParams$$.put($S, $L)", p.baseName(), p.name()));
            urlBuildExpr = ".buildFromMap(pathParams$)";
        }
        final var httpMethod = operationOpt.map(ApiWiserBundle.CodeOperation.Op::httpMethod).orElse("NOT_SPECIFIED");
        final var bodyParams = operationOpt
                .stream()
                .map(ApiWiserBundle.CodeOperation.Op::allParams)
                .flatMap(Collection::stream)
                .filter(ApiWiserBundle.CodeParameter::isBodyParam)
                .toList();
        final var requestBodyExprBuilder = CodeBlock.builder();
        if (bodyParams.isEmpty()) {
            if (Set.of("POST", "PUT", "PATCH").contains(httpMethod)) {
                requestBodyExprBuilder.add("$T.noBody()", HttpRequest.BodyPublishers.class);
            }
        } else {
            requestBodyExprBuilder
                    .add("$T.ofString(jackson$$.writeValueAsString($L))",
                            HttpRequest.BodyPublishers.class, bodyParams.get(0).name());
        }
        final var requestBodyExpr = requestBodyExprBuilder.build();
        result
                .addStatement(CodeBlock
                        .builder()
                        .add("final var request$$ = $T.newBuilder()", HttpRequest.class)
                        .add(".uri(url$$$L)\n", urlBuildExpr)
                        .indent()
                        .add( "PATCH".equals(httpMethod)
                                        || ("DELETE".equals(httpMethod) && !requestBodyExpr.isEmpty())
                                ? ".method(\"$L\", $L)"
                                : ".$L($L)"
                                , httpMethod, requestBodyExpr)
                        .unindent()
                        .build())
                .addStatement("final var response$$ = httpClient$$.send(request$$.build(), $L)", httpSendCases(operationOpt))
                .add(httpResultCases(operationOpt))
                .nextControlFlow("catch($T e$$)", IOException.class)
                .addStatement("throw new $T(e$$)", UncheckedIOException.class)
                .nextControlFlow("catch($T e$$)", InterruptedException.class)
                .addStatement("$T.currentThread().interrupt()", Thread.class)
                .addStatement("throw new HttpClientInterruptedException(e$$)")
                .endControlFlow()
                ;
        return result.build();
    }

    /**
     * Generates the HTTP response handling code.
     * 
     * <p>This method creates the code block that handles the HTTP response processing.
     * It includes logic for handling different HTTP status codes and deserializing the response body.</p>
     * 
     * @param operationOpt the optional API operation to generate response handling for
     * @return the code block for HTTP response handling
     */
    static CodeBlock httpSendCases(Optional<ApiWiserBundle.CodeOperation.Op> operationOpt) {
        final var operationId = operationOpt.map(ApiWiserBundle.CodeOperation.Op::operationId).orElse("");
        final var returnType = operationOpt.map(ApiWiserBundle.CodeOperation.Op::returnType).orElse("Void");
        final var builder = CodeBlock.builder()
                .beginControlFlow("info$$ ->")
                .beginControlFlow("if (!(null == preResponseHandler$$ || preResponseHandler$$.apply($S, info$$)))", operationId)
                .add("return $T.mapping($T.ofInputStream(), inputStream$$ -> \n", HttpResponse.BodySubscribers.class, HttpResponse.BodySubscribers.class)
                .indent()
                .add("() -> new HttpClientResponseWrapper<>(new byte[]{}));\n")
                .unindent()
                .endControlFlow()
                .beginControlFlow("if (200 == info$$.statusCode())")
                .add("return $T.mapping($T.ofInputStream(), inputStream$$ -> \n", HttpResponse.BodySubscribers.class, HttpResponse.BodySubscribers.class)
                .indent()
                .add("($T<HttpClientResponseWrapper<$L>>) () -> {\n", Supplier.class, returnType)
                .indent()
                .add("try(final var stream$$ = inputStream$$) {\n")
                .indent()
                .add("return new HttpClientResponseWrapper<>(jackson$$.readValue(stream$$, new $T<$L>() {}));\n", TypeReference.class, returnType)
                .unindent()
                .add("} catch(IOException e) {\n")
                .indent()
                .add("throw new $T(e);\n", UncheckedIOException.class)
                .unindent()
                .add("}\n")
                .unindent()
                .add("});\n")
                .unindent()
                .nextControlFlow("else")
                .add("return $T.mapping($T.ofByteArray(), content$$ ->\n", HttpResponse.BodySubscribers.class, HttpResponse.BodySubscribers.class)
                .indent()
                .add("() -> new HttpClientResponseWrapper<>(content$$) );\n")
                .unindent()
                .endControlFlow()
                .endControlFlow()
                ;

        return builder.build();
    }

    /**
     * Generates the HTTP result processing code.
     * 
     * <p>This method creates the code block that processes the HTTP response result.
     * It includes logic for handling different HTTP status codes and returning the appropriate result.</p>
     * 
     * @param operationOpt the optional API operation to generate result processing for
     * @return the code block for HTTP result processing
     */
    static CodeBlock httpResultCases(Optional<ApiWiserBundle.CodeOperation.Op> operationOpt) {
        final var returnType = operationOpt.map(ApiWiserBundle.CodeOperation.Op::returnType).orElse(null);
        final var builder = CodeBlock.builder()
                .beginControlFlow("switch(response$$.statusCode())")
                .add("case 200: $L;\n", null == returnType ? "break" : "return response$.body().get().getBody()")
                .add("default: throw new HttpClientDefaultException(\"Http Status: \" + response$$.statusCode() + \" - \" + response$$.body().get().getText());\n")
                ;

        return builder.endControlFlow().build();
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
        final var result = ParameterSpec.builder(TypeRefInfo.parse(parameter.dataType(), extendedImports).toTypeName(), parameter.name());
        return result.build();
    }
}
