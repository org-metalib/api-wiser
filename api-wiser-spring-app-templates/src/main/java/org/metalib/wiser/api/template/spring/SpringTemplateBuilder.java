package org.metalib.wiser.api.template.spring;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.AnnotationSpec.Builder;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.javapoet.TypeRefInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

import javax.lang.model.element.Modifier;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.metalib.wiser.api.template.ApiWiserFinals.BIZ;
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
    Optional.of(operation)
        .map(ApiWiserBundle.CodeOperation.Op::allParams)
        .stream()
        .flatMap(Collection::stream)
        .forEach(p -> method.addParameter(factualParameter(p, extendedImports)));
    return method
        .addCode(CodeBlock.builder()
            .addStatement("$L super.$L($L)",
                "void".equals(returnType) || null == returnType ? "" : "return",
                operation.operationId(), Optional.of(operation)
                    .map(ApiWiserBundle.CodeOperation.Op::allParams)
                    .stream()
                    .flatMap(Collection::stream)
                    .map(ApiWiserBundle.CodeParameter::name)
                    .collect(joining(", ")))
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
