package org.metalib.wiser.api.template.biz;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.javapoet.TypeRefInfo;
import org.metalib.wiser.api.template.api.ApiTemplate;
import org.metalib.wiser.api.template.model.ApiWiserBean;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.openapitools.codegen.utils.StringUtils.camelize;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BizBuilder {

  public static TypeSpec.Builder createBizBuilder(ApiWiserBundle bundle) {
    final var className = camelize(bundle.baseName()) + camelize(BizTemplate.TEMPLATE_NAME);
    final var classBuilder = TypeSpec
        .classBuilder(className)
        .addSuperinterface(ClassName.get(bundle.basePackage() + DOT + ApiTemplate.TEMPLATE_NAME,
                bundle.camelizeBaseName() + camelize(ApiTemplate.TEMPLATE_NAME)))
        .addModifiers(Modifier.PUBLIC);

    final var beanContext = bundle.beanContext();
    final var constructorVariables = beanContext.beans();
    if (null == constructorVariables || constructorVariables.isEmpty()) {
      classBuilder.addAnnotation(NoArgsConstructor.class);
    } else {
      classBuilder.addAnnotation(RequiredArgsConstructor.class);
      final var imports = new HashSet<>(beanContext.imports());
      constructorVariables.forEach(v -> classBuilder.addField(bizFieldSpec(v, imports)));
    }

    final var imports = bundle.imports();
    bundle.codeOperation()
        .map(ApiWiserBundle.CodeOperation::operations)
        .stream()
        .flatMap(Collection::stream)
        .forEach(o -> classBuilder.addMethod(bizMethodSpec(o, imports)));
    return classBuilder;
  }

  static FieldSpec bizFieldSpec(ApiWiserBean bean, Set<String> imports) {
    return FieldSpec.builder(TypeRefInfo.parse(bean.type(), imports).toTypeName(), bean.name(), Modifier.FINAL).build();
  }

  private static MethodSpec bizMethodSpec(ApiWiserBundle.CodeOperation.Op operation, Set<String> imports) {
    final var extendedImports = new HashSet<>(imports);
    final var className = operation.baseName();
    final var methodName = operation.operationId();
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
        .methodBuilder(methodName)
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Override.class)
        .addCode(CodeBlock.builder()
            .addStatement("throw new $T(\"method $L::$L not implemented.\")", UnsupportedOperationException.class, className, methodName)
            .build())
        .returns(TypeRefInfo.parse(operation.returnType(), extendedImports).toTypeName());
    Optional.of(operation)
        .map(ApiWiserBundle.CodeOperation.Op::allParams)
        .stream()
        .flatMap(Collection::stream)
        .forEach(p -> method.addParameter(ParameterSpec
            .builder(TypeRefInfo.parse(p.dataType(), extendedImports).toTypeName(), p.name())
            .build()));
    return method.build();
  }

}
