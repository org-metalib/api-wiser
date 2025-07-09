package org.metalib.wiser.api.template.spring;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.AnnotationSpec.Builder;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.javapoet.TypeRefInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.metalib.wiser.api.template.ApiWiserFinals.BIZ;
import static org.metalib.wiser.api.template.ApiWiserFinals.QUERY;
import static org.metalib.wiser.api.template.ApiWiserFinals.capitalizeWithQuery;
import static org.springframework.util.StringUtils.capitalize;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringTemplateBuilder {

  public static final String MODULE_NAME = "spring-app";

  public static final String SPRING_APP = "SpringApp";
  public static final String CONTROLLER = "controller";
  public static final String DOT = ".";
  public static final String DASH = "-";
  public static TypeSpec.Builder createSpringAppClassBuilder(ApiWiserBundle openApiModel) {
    final var className = SPRING_APP;
    return  TypeSpec
        .classBuilder(className)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(SpringBootApplication.class)
        .addMethod(MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
            .addParameter(ParameterSpec.builder(String[].class, "args").build())
            .addStatement(CodeBlock.of("$T.run($L.class, args)", SpringApplication.class, className))
            .build());
  }

  static void requestParamMappingStatement(CodeBlock.Builder builder, ApiWiserBundle.CodeParameter p) {
    final var type = p.dataType();
    if ("String".equals(type)) {
      builder.addStatement("request.map(v -> v.getParameter($S)).ifPresent(result::$L);", p.baseName(), p.name());
    } else if ("BigDecimal".equals(type)){
      builder.addStatement("request.map(v -> v.getParameter($S)).map($T::new).ifPresent(result::$L);", p.baseName(), BigDecimal.class, p.name());
    } else if ("LocalDate".equals(type)){
      builder.addStatement("request.map(v -> v.getParameter($S)).map($T::parse).ifPresent(result::$L);", p.baseName(), LocalDate.class, p.name());
    } else {
      builder.addStatement("request.map(v -> v.getParameter($S)).map($L::valueOf).ifPresent(result::$L);", p.baseName(), type, p.name());
    }
  }

  public static TypeSpec.Builder createSpringControllerBuilder(ApiWiserBundle bundle) {
    final var operations = bundle.codeOperation();
    final var className = bundle.camelizeBaseName() + capitalize(CONTROLLER);
    final var imports = bundle.imports();
    final var commonPath = bundle.commonPath();
    final var restControllerAnnotation = AnnotationSpec.builder(RestController.class);
    final var classBuilder = TypeSpec
        .classBuilder(className)
        .superclass(ClassName.get(bundle.basePackage() + DOT + BIZ, bundle.camelizeBaseName() + "Biz"))
        .addAnnotation(restControllerAnnotation.build())
        .addModifiers(Modifier.PUBLIC);

    operations
        .map(ApiWiserBundle.CodeOperation::operations)
        .stream()
        .flatMap(Collection::stream)
        .forEach(o -> classBuilder.addMethod(apiMethodSpec(o, commonPath, imports)));

    final var queryParamResolverClasses = new ArrayList<TypeSpec>();
    final var queryParamArgumentResolverAddCodeBlockBuilder = CodeBlock.builder();
    operations
            .map(ApiWiserBundle.CodeOperation::operations)
            .stream()
            .flatMap(Collection::stream)
            .filter(o -> o.queryParams().size() > o.queryParamMaxInLine())
            .forEach(o -> {
              final var operationId = o.operationId();
              final var operationIdQueryClassName = capitalizeWithQuery(operationId);
              final var operationIdResolverClassName = operationIdQueryClassName + "Resolver";
              final var paramMappingCodeBlockBuilder = CodeBlock.builder();
              o.queryParams().forEach(p -> requestParamMappingStatement(paramMappingCodeBlockBuilder, p));
              queryParamResolverClasses.add(TypeSpec.classBuilder(operationIdResolverClassName)
                      .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                      .addSuperinterface(HandlerMethodArgumentResolver.class)
                      .addMethod(MethodSpec.methodBuilder("supportsParameter")
                              .addModifiers(Modifier.PUBLIC)
                              .addAnnotation(Override.class)
                              .returns(TypeName.BOOLEAN)
                              .addParameter(MethodParameter.class, "p")
                              .addStatement("return p.getParameterType() == $L.class", operationIdQueryClassName)
                              .build())
                      .addMethod(MethodSpec.methodBuilder("resolveArgument")
                              .addModifiers(Modifier.PUBLIC)
                              .addAnnotation(Override.class)
                              .returns(TypeName.get(Object.class))
                              .addParameter(MethodParameter.class, "p")
                              .addParameter(ModelAndViewContainer.class, "mav")
                              .addParameter(NativeWebRequest.class, "r")
                              .addParameter(WebDataBinderFactory.class, "bf")
                              .addStatement("final var request = $T.of(r)", Optional.class)
                              .addStatement("final var result = $L.builder()", operationIdQueryClassName)
                              .addCode(paramMappingCodeBlockBuilder.build())
                              .addStatement("return result.build()")
                              .build())
                      .build());
              queryParamArgumentResolverAddCodeBlockBuilder.addStatement("resolvers.add(new $L())", operationIdResolverClassName);
            });

    if (!queryParamResolverClasses.isEmpty()) {
      classBuilder.addTypes(queryParamResolverClasses);
      classBuilder
              .addType(TypeSpec.classBuilder("Config")
                      .addAnnotation(Configuration.class)
                      .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                      .addSuperinterface(WebMvcConfigurer.class)
                      .addMethod(MethodSpec.methodBuilder("addArgumentResolvers")
                              .addModifiers(Modifier.PUBLIC)
                              .addAnnotation(Override.class)
                              .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(List.class, HandlerMethodArgumentResolver.class), "resolvers").build())
                              .addCode(queryParamArgumentResolverAddCodeBlockBuilder.build())
                              .build())
                      .build());
    }
    return classBuilder;
  }

  private static MethodSpec apiMethodSpec(ApiWiserBundle.CodeOperation.Op operation, String commonPath, Set<String> imports) {
    final var queryParamMaxInLine = operation.queryParamMaxInLine();
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
    final var method = MethodSpec.methodBuilder(operation.operationId());
    operationOpt
        .map(ApiWiserBundle.CodeOperation.Op::httpMethod)
        .map(SpringTemplateBuilder::httpMethodOf)
        .map(AnnotationSpec::builder)
        .map(v -> decorateRestAnnotation(v, operationOpt, commonPath, imports))
        .map(Builder::build)
        .ifPresent(method::addAnnotation);
    method
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .returns(TypeRefInfo.parse(returnType, extendedImports).toTypeName());

    final var factualParamNames = new ArrayList<String>();

    // First, always add all body params, usually just one.
    operationOpt
            .stream()
            .map(ApiWiserBundle.CodeOperation.Op::bodyParams)
            .flatMap(Collection::stream)
            .forEach(p -> {
              final var name = p.name();
              method.addParameter(ParameterSpec.builder(TypeRefInfo.parse(p.dataType(), extendedImports).toTypeName(), name).build());
              factualParamNames.add(name);
            });

    // Second, we respect the path params
    operationOpt
            .stream()
            .map(ApiWiserBundle.CodeOperation.Op::pathParams)
            .flatMap(Collection::stream)
            .forEach(p -> {
              final var name = p.name();
              method.addParameter(ParameterSpec.builder(TypeRefInfo.parse(p.dataType(), extendedImports).toTypeName(), name).build());
              factualParamNames.add(name);
            });

    // Finally, we add the query parameters! Since there may be several, let’s organize them into a single class for better management.
    final var operationId = operation.operationId();
    final var queryParams = Optional.of(operation).map(ApiWiserBundle.CodeOperation.Op::queryParams).stream().flatMap(Collection::stream).toList();
    if (queryParams.isEmpty()) {
    } else if (queryParams.size() < queryParamMaxInLine) {
      queryParams.forEach(p -> method.addParameter(factualParameter(p, extendedImports)));
      factualParamNames.addAll(Optional.of(operation)
              .map(ApiWiserBundle.CodeOperation.Op::queryParams)
              .stream()
              .flatMap(Collection::stream)
              .map(ApiWiserBundle.CodeParameter::name)
              .toList());
    } else {
      method.addParameter(ParameterSpec.builder(TypeRefInfo.parse(capitalizeWithQuery(operationId).toString(), imports)
              .toTypeName(), QUERY).build());
      factualParamNames.add(QUERY);
    }
    return method.addCode(CodeBlock.builder()
                    .addStatement("$L super.$L($L)", "void".equals(returnType) || null == returnType ? "" : "return",
                            operationId, String.join(", ", factualParamNames))
                    .build())
            .build();
  }

  static ParameterSpec factualParameter(ApiWiserBundle.CodeParameter parameter, Set<String> extendedImports) {
    final var result = ParameterSpec.builder(TypeRefInfo.parse(parameter.dataType(), extendedImports).toTypeName(), parameter.name());
    if (parameter.isBodyParam()) {
      result.addAnnotation(RequestBody.class);
    }
    if (parameter.isQueryParam()) {
      result.addAnnotation(AnnotationSpec
          .builder(RequestParam.class)
          .addMember("value", "$S", parameter.name())
          .build());
    }
    if (parameter.isPathParam()) {
      result.addAnnotation(AnnotationSpec
          .builder(PathVariable.class)
          .addMember("value", "$S", parameter.name())
          .build());
    }
    return result.build();
  }

  static AnnotationSpec.Builder decorateRestAnnotation(AnnotationSpec.Builder builder,
                                                       Optional<ApiWiserBundle.CodeOperation.Op> operationOpt,
                                                       String commonPath,
                                                       Set<String> imports) {
    operationOpt
        .map(ApiWiserBundle.CodeOperation.Op::path)
        .filter(v -> !v.isBlank())
        .ifPresent(path -> builder.addMember("value", "$S", path));
    final var consumes = operationOpt
        .map(ApiWiserBundle.CodeOperation.Op::consumes)
        .stream()
        .flatMap(Collection::stream)
        .map(ApiWiserBundle.MediaType::name)
        .toList();
    if (!consumes.isEmpty()) {
      builder.addMember("consumes", "$L", consumes
          .stream()
          .map(v -> CodeBlock.of("$S", v))
          .collect(CodeBlock.joining(",", "{", "}")));
    }
    final var produces = operationOpt
        .map(ApiWiserBundle.CodeOperation.Op::produces)
        .stream()
        .flatMap(Collection::stream)
        .map(ApiWiserBundle.MediaType::name)
        .toList();
    if (!produces.isEmpty()) {
      builder.addMember("produces", "$L", produces.stream()
          .map(v -> CodeBlock.of("$S", v))
          .collect(CodeBlock.joining(",", "{", "}")));
    }
    return builder;
  }

  static Class<? extends Annotation> httpMethodOf(String httpMethod) {
    switch (RequestMethod.valueOf(httpMethod)) {
      case GET: return GetMapping.class;
      case POST: return PostMapping.class;
      case PUT: return PutMapping.class;
      case DELETE: return DeleteMapping.class;
      case PATCH: return PatchMapping.class;
      default:
        throw new IllegalArgumentException(format("Invalid HTTP Method: %s", httpMethod));
    }
  }
}
