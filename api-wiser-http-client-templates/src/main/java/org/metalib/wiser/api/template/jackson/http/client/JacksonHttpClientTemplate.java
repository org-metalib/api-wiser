package org.metalib.wiser.api.template.jackson.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
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
import java.util.stream.Collectors;

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

public class JacksonHttpClientTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "jackson-http-client";
    public static final String TEMPLATE_ID = API_WISER + DOUBLE_COLON + TEMPLATE_NAME;

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    @Override
    public boolean isApiFile() {
        return true;
    }

    @Override
    public String fileExtension() {
        return JAVA;
    }

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

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var httpClientPackage = bundle.basePackage() + DOT + HTTP + DOT + CLIENT;
        return JavaFile.builder(httpClientPackage, httpClientTypeBuilder(bundle).build())
                .build().toString();
    }

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
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
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
                .addStatement("throw new $T(e$$)", RuntimeException.class)
                .endControlFlow()
                ;
        return result.build();
    }

    static CodeBlock httpSendCases(Optional<ApiWiserBundle.CodeOperation.Op> operationOpt) {
        final var operationId = operationOpt.map(ApiWiserBundle.CodeOperation.Op::operationId).orElse("");
        final var returnType = operationOpt.map(ApiWiserBundle.CodeOperation.Op::returnType).orElse("Void");
        final var builder = CodeBlock.builder()
                .beginControlFlow("info$$ ->")
                .beginControlFlow("if (!(null == preResponseHandler$$ || preResponseHandler$$.apply($S, info$$)))", operationId)
                .add("return $T.mapping($T.ofInputStream(), inputStream$$ -> \n", HttpResponse.BodySubscribers.class, HttpResponse.BodySubscribers.class)
                .indent()
                .add("() -> new ResponseWrapper<>(new byte[]{}));\n")
                .unindent()
                .endControlFlow()
                .beginControlFlow("if (200 == info$$.statusCode())")
                .add("return $T.mapping($T.ofInputStream(), inputStream$$ -> \n", HttpResponse.BodySubscribers.class, HttpResponse.BodySubscribers.class)
                .indent()
                .add("($T<ResponseWrapper<$L>>) () -> {\n", Supplier.class, returnType)
                .indent()
                .add("try(final var stream$$ = inputStream$$) {\n")
                .indent()
                .add("return new ResponseWrapper<>(jackson$$.readValue(stream$$, new $T<$L>() {}));\n", TypeReference.class, returnType)
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
                .add("() -> new ResponseWrapper<>(content$$) );\n")
                .unindent()
                .endControlFlow()
                .endControlFlow()
                ;

        return builder.build();
    }

    static CodeBlock httpResultCases(Optional<ApiWiserBundle.CodeOperation.Op> operationOpt) {
        final var returnType = operationOpt.map(ApiWiserBundle.CodeOperation.Op::returnType).orElse(null);
        final var builder = CodeBlock.builder()
                .beginControlFlow("switch(response$$.statusCode())")
                .add("case 200: $L;\n", null == returnType ? "break" : "return response$.body().get().getBody()")
                .add("default: throw new RuntimeException(\"Http Status: \" + response$$.statusCode() + \" - \" + response$$.body().get().getText());\n")
                ;

        return builder.endControlFlow().build();
    }

    static ParameterSpec factualParameter(ApiWiserBundle.CodeParameter parameter, Set<String> extendedImports) {
        final var result = ParameterSpec.builder(TypeRefInfo.parse(parameter.dataType(), extendedImports).toTypeName(), parameter.name());
        return result.build();
    }

}
