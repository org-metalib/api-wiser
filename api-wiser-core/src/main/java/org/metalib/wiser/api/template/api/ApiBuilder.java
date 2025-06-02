package org.metalib.wiser.api.template.api;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.javapoet.TypeRefInfo;
import org.openapitools.codegen.utils.StringUtils;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiBuilder {
  public static TypeSpec.Builder createApiBuilder(ApiWiserBundle openApiModel) {
    final var operations = openApiModel.codeOperation();
    final var className = operations.map(ApiWiserBundle.CodeOperation::className)
        .map(StringUtils::camelize)
        .orElse("{Class Name not provided}");
    final var imports = openApiModel.imports();

    final var classBuilder = TypeSpec
        .interfaceBuilder(className)
        .addModifiers(Modifier.PUBLIC);
    operations
        .map(ApiWiserBundle.CodeOperation::operations)
        .stream()
        .flatMap(Collection::stream)
        .forEach(o -> classBuilder.addMethod(apiMethodSpec(o, imports)));
    return classBuilder;
  }

  private static MethodSpec apiMethodSpec(ApiWiserBundle.CodeOperation.Op operation, Set<String> imports) {
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
    final var method = MethodSpec
        .methodBuilder(operation.operationId())
        .addModifiers(Modifier.ABSTRACT, Modifier.PUBLIC)
        .returns(TypeRefInfo.parse(operation.returnType(), extendedImports).toTypeName());
    Optional.of(operation)
        .map(ApiWiserBundle.CodeOperation.Op::allParams)
        .stream()
        .flatMap(Collection::stream)
        .forEach(p -> method.addParameter(factualParameter(p, extendedImports)));
    return method.build();
  }

  static ParameterSpec factualParameter(ApiWiserBundle.CodeParameter parameter, Set<String> extendedImports) {
    final var result = ParameterSpec
        .builder(TypeRefInfo.parse(parameter.dataType(), extendedImports).toTypeName(), parameter.name());
    return result.build();
  }

}
