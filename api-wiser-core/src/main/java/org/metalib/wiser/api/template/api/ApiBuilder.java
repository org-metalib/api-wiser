package org.metalib.wiser.api.template.api;

import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.javapoet.TypeRefInfo;
import org.openapitools.codegen.utils.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.metalib.wiser.api.template.ApiWiserFinals.QUERY;
import static org.metalib.wiser.api.template.ApiWiserFinals.QUERY_CAPITALISED;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiBuilder {

  public static TypeSpec.Builder createApiBuilder(ApiWiserBundle openApiModel) {
    final var operations = openApiModel.codeOperation();
    final var className = operations.map(ApiWiserBundle.CodeOperation::className)
        .map(StringUtils::camelize)
        .orElse("{Class Name not provided}");
    final var imports = openApiModel.imports();

    final var classBuilder = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
    operations
            .map(ApiWiserBundle.CodeOperation::operations)
            .stream()
            .flatMap(Collection::stream)
            .forEach(o -> classBuilder.addMethod(apiMethodSpec(o, imports)));
    operations
            .map(ApiWiserBundle.CodeOperation::operations)
            .stream()
            .flatMap(Collection::stream)
            .forEach(o -> apiQueryClass(classBuilder, o, imports));
    return classBuilder;
  }

  private static void apiQueryClass(TypeSpec.Builder classBuilder, ApiWiserBundle.CodeOperation.Op operation, Set<String> imports) {
    final var queryParamMaxInLine = operation.queryParamMaxInLine();
    final var operationId = operation.operationId();
    final var queryParams = Optional.of(operation).map(ApiWiserBundle.CodeOperation.Op::queryParams);
    queryParams.ifPresent(qParams -> {
      if (queryParamMaxInLine <= qParams.size()) {
        classBuilder.addType(TypeSpec.classBuilder(capitalize(operationId) + QUERY_CAPITALISED)
                .addAnnotation(Data.class)
                .addAnnotation(AllArgsConstructor.class)
                .addAnnotation(NoArgsConstructor.class)
                .addAnnotation(AnnotationSpec.builder(Builder.class).addMember("toBuilder", "$L", true).build())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addFields(qParams.stream().map(v ->
                        FieldSpec.builder(TypeRefInfo.parse(v.dataType(), imports).toTypeName(), v.name()).build()).toList())
                .build());
      }
    });
  }

  private static MethodSpec apiMethodSpec(ApiWiserBundle.CodeOperation.Op operation, Set<String> imports) {
    final var queryParamMaxInLine = operation.queryParamMaxInLine();
    final var extendedImports = new HashSet<>(imports);
    final var operationOpt = Optional.of(operation);
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
            .flatMap(v -> v.allParams().stream())
            .anyMatch(v -> v.dataType().startsWith("Map<"))) {
      extendedImports.add("java.util.Map");
    }

    final var operationId = operation.operationId();
    final var method = MethodSpec
        .methodBuilder(operationId)
        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
        .returns(TypeRefInfo.parse(operation.returnType(), extendedImports).toTypeName());

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
        method.addParameter(ParameterSpec.builder(TypeRefInfo.parse(capitalize(operationId) + QUERY_CAPITALISED, imports).toTypeName(), QUERY).build());
      }
    });
    return method.build();
  }

  static ParameterSpec factualParameter(ApiWiserBundle.CodeParameter parameter, Set<String> extendedImports) {
    final var result = ParameterSpec
        .builder(TypeRefInfo.parse(parameter.dataType(), extendedImports).toTypeName(), parameter.name());
    return result.build();
  }

}
