package org.metalib.wiser.api.template.model.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.javapoet.AnnotationSpec;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.javapoet.TypeRefInfo;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class JacksonModelBuilder {

  ApiWiserBundle bundle;
  ApiWiserBundle.CodeModel model;
  ApiWiserBundle.CodeProperty property;
  Set<String> reservedFieldNames;

  String reserveFieldName(String name) {
    var counter = 0;
    var result = name;
    while (reservedFieldNames.contains(result)) {
      result = name + counter;
    }
    reservedFieldNames.add(result);
    return result;
  }

  FieldSpec fieldSpec() {
    final var propertyOpt = Optional.of(property);
    final var type = property.datatypeWithEnum();
    final var base = property.baseName();
    final var name = property.name();
    assert null != type && !type.isBlank();
    assert null != name && !name.isBlank();
    final var imports = imports();
    final var fieldSpec = FieldSpec.builder(TypeRefInfo.parse(type, imports()).toTypeName(), reserveFieldName(name));
    propertyOpt
        .map(ApiWiserBundle.CodeProperty::defaultValue)
        .filter(v -> !"null".equals(v) && !v.isBlank())
        .ifPresent(v -> fieldSpec
            .initializer("$L", applyImport(v, imports)));
    propertyOpt
        .map(ApiWiserBundle.CodeProperty::description)
        .filter(v -> !v.isBlank())
        .ifPresent(description -> fieldSpec
            .addJavadoc(CodeBlock.builder()
                .add("$L", description)
                .build()));
    final var jsonProperty = AnnotationSpec.builder(JsonProperty.class)
        .addMember("value", "$S", base);
    propertyOpt.filter(ApiWiserBundle.CodeProperty::required)
        .ifPresent(v -> jsonProperty
            .addMember("required", "$L", true));
    return fieldSpec
        .addAnnotation(jsonProperty.build())
        .build();
  }

  Set<String> imports() {
    return bundle.imports();
  }

  static String applyImport(String expression, Collection<String> imports) {
    final var javaId = new StringBuilder();
    final var result = new StringBuilder();
    for (final var ch : expression.toCharArray()) {
      if (Character.isJavaIdentifierPart(ch) || ch == '.') {
        javaId.append(ch);
      } else {
        if (0 < javaId.length()) {
          final var id = javaId.toString();
          result.append(imports.stream().filter(v -> v.endsWith(id)).findFirst().orElse(id));
          javaId.setLength(0);
        }
        result.append(ch);
      }
    }
    if (0 < javaId.length()) {
      final var id = javaId.toString();
      result.append(imports.stream().filter(v -> v.endsWith(id)).findFirst().orElse(id));
    }
    return result.toString();
  }

  public static Set<String> imports(Optional<Map<String, Object>> bundle) {
    return bundle
        .map(v -> (List<Map<String,String>>)v.get("imports"))
        .stream()
        .flatMap(Collection::stream)
        .map(v -> v.get("import"))
        .collect(Collectors.toSet());
  }

  static TypeSpec.Builder buildModelEnum(ApiWiserBundle bundle) {
    final var enumName = bundle.className();
    final var modelOpt = bundle.codeModel();
    final var enumBuilder = TypeSpec
        .enumBuilder(enumName)
        .addAnnotation(Getter.class)
        .addAnnotation(RequiredArgsConstructor.class)
        .addModifiers(Modifier.PUBLIC);
    modelOpt
        .map(ApiWiserBundle.CodeModel::description)
        .filter(v -> !v.isBlank())
        .ifPresent(description -> enumBuilder
            .addJavadoc(CodeBlock.builder()
                .add("$L", description)
                .build()));
    modelOpt
            .map(ApiWiserBundle.CodeModel::enumVars)
            .stream().flatMap(Collection::stream)
            .forEach(v ->
            enumBuilder.addEnumConstant(v.name(),
                    TypeSpec.anonymousClassBuilder("$L", v.value().toString()).build())
        );
    modelOpt.map(ApiWiserBundle.CodeModel::dataType)
        .ifPresent(type ->  buildModelEnum(enumBuilder, enumName, type, bundle.imports()));
    return enumBuilder;
  }

  static TypeSpec.Builder buildInnerEnum(ApiWiserBundle bundle, Optional<ApiWiserBundle.CodeProperty> modelOpt) {
    final var enumName = modelOpt.map(ApiWiserBundle.CodeProperty::enumName).orElse("ENUM NAME NOT PROVIDED");
    final var enumBuilder = TypeSpec
        .enumBuilder(enumName)
        .addAnnotation(Getter.class)
        .addAnnotation(RequiredArgsConstructor.class)
        .addModifiers(Modifier.PUBLIC);
    modelOpt
        .map(ApiWiserBundle.CodeProperty::description)
        .filter(v -> !v.isBlank())
        .ifPresent(description -> enumBuilder
            .addJavadoc(CodeBlock.builder()
                .add("$L", description)
                .build()));
    modelOpt
        .map(ApiWiserBundle.CodeProperty::enumVars)
        .stream()
        .flatMap(Collection::stream).forEach(v ->
            enumBuilder.addEnumConstant(v.name(),
                TypeSpec.anonymousClassBuilder("$L", v.value().toString()).build())
        );
    modelOpt.map(ApiWiserBundle.CodeProperty::dataType)
        .ifPresent(type -> buildModelEnum(enumBuilder, enumName, type, bundle.imports()));
    return enumBuilder;
  }

  static void buildModelEnum(TypeSpec.Builder enumBuilder, String enumName, String type, Set<String> imports) {
    final var typeName = TypeRefInfo.parse(type, imports).toTypeName();
    enumBuilder
        .addField(FieldSpec.builder(typeName,
                "value", Modifier.FINAL, Modifier.PRIVATE)
            .addAnnotation(JsonValue.class)
            .build())
        .addMethod(MethodSpec.methodBuilder("fromValue")
            .addJavadoc(CodeBlock.builder().add("$L", "Deserialization Factory method").build())
            .addAnnotation(JsonCreator.class)
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
            .addParameter(ParameterSpec.builder(typeName, "value", Modifier.FINAL)
                .addJavadoc("$N", "Serialized enum value.")
                .build())
            .addCode(CodeBlock.builder()
                .addStatement("return $T.stream(values())\n"
                              + ".filter(v -> v.value.equals(value))\n"
                              + ".findFirst()\n"
                              + ".orElseThrow(() -> new IllegalArgumentException(\"Unexpected value '\" + value + \"'\"))",
                    ClassName.get("java.util", "Arrays"))
                .build())
            .returns(ClassName.bestGuess(enumName))
            .build());

  }

  static TypeSpec.Builder buildModelClass(ApiWiserBundle openApiModelOpt) {
    final var className = openApiModelOpt.className();
    final var packageName = openApiModelOpt.bundlePackage();
    final var modelOpt = openApiModelOpt.codeModel();
    final var fieldNames = modelOpt
            .map(ApiWiserBundle.CodeModel::vars)
            .stream()
            .flatMap(Collection::stream)
            .map(ApiWiserBundle.CodeProperty::baseName)
            .map(v -> '"' + v + '"')
            .collect(joining(", "));
    final var fieldOrderAnnotation = AnnotationSpec.builder(JsonPropertyOrder.class)
        .addMember("value", "$L", format("{%s}", fieldNames))
        .build();
    final var fieldSpecs = modelOpt
        .stream()
        .flatMap(v -> streamOfFieldSpecs(JacksonModelBuilder.builder()
            .bundle(openApiModelOpt)
            .model(v)
            .build()))
        .collect(toList());
    final var classBuilder = TypeSpec.classBuilder(className);
    if (!fieldSpecs.isEmpty()) {
      classBuilder
          .addAnnotation(Data.class)
          .addAnnotation(AllArgsConstructor.class);
    }
    classBuilder
        .addAnnotation(NoArgsConstructor.class)
        .addAnnotation(AnnotationSpec.builder(modelOpt
                .filter(v -> null != v.parentModel()
                             || (null != v.children() && !v.children().isEmpty()))
                .map(v -> (Class)SuperBuilder.class)
                .orElse(Builder.class))
            .addMember("toBuilder", "$L", true)
            .build())
        .addAnnotation(fieldOrderAnnotation)
        .addAnnotation(AnnotationSpec.builder(JsonTypeName.class)
            .addMember("value", "$S",
                modelOpt.map(ApiWiserBundle.CodeModel::name).orElse(className))
            .build());
    modelOpt
        .map(ApiWiserBundle.CodeModel::description)
        .filter(v -> !v.isBlank())
        .ifPresent(description -> classBuilder
            .addJavadoc(CodeBlock.builder()
                .add("$L", description)
                .build()));
    modelOpt
        .map(ApiWiserBundle.CodeModel::discriminator)
        .ifPresent(discriminator -> classBuilder
            .addAnnotation(AnnotationSpec.builder(JsonTypeInfo.class)
                .addMember("use", "$L", "JsonTypeInfo.Id.NAME")
                .addMember("include", "$L", "JsonTypeInfo.As.PROPERTY")
                .addMember("property", "$S", discriminator.propertyName())
                .addMember("visible", "$L", true)
                .build()));
    modelOpt
        .map(ApiWiserBundle.CodeModel::children)
        .filter(v -> !v.isEmpty())
        .ifPresent(children -> {
          final var jsonSubTypes = AnnotationSpec.builder(JsonSubTypes.class);
          children.stream().map(ApiWiserBundle.CodeModel::name).distinct().sorted()
              .forEach(childClassName -> jsonSubTypes
                  .addMember("value", "$L", AnnotationSpec
                      .builder(JsonSubTypes.Type.class)
                      .addMember("value", "$L", childClassName + ".class")
                      .addMember("name", "$S", childClassName)
                      .build()));
          classBuilder
              .addAnnotation(jsonSubTypes.build());
        });
    modelOpt
        .map(ApiWiserBundle.CodeModel::parentModel)
        .ifPresent(parent -> classBuilder
            .superclass(ClassName.get(packageName, parent.name())));
    classBuilder
        //.addAnnotation(Jacksonized.class)
        .addAnnotation(AnnotationSpec.builder(Generated.class)
            .addMember("value", "$S", "lombok-openapi")
            .addMember("date", "$S", Instant.now())
            .build())
        .addModifiers(Modifier.PUBLIC)
        .addFields(fieldSpecs);
    streamOfCodegenProperties(modelOpt)
        .filter(ApiWiserBundle.CodeProperty::isInnerEnum)
        .forEach(p ->
          classBuilder.addType(buildInnerEnum(openApiModelOpt,
              Optional.of(null == p.items() ? p : p.items())).build()));
    return classBuilder;
  }

  static Stream<ApiWiserBundle.CodeProperty> streamOfCodegenProperties(Optional<ApiWiserBundle.CodeModel> modelOpt) {
    return modelOpt
        .map(ApiWiserBundle.CodeModel::vars)
        .stream()
        .flatMap(Collection::stream);
  }

  static Stream<FieldSpec> streamOfFieldSpecs(JacksonModelBuilder ctx) {
    final var reservedFieldNames = new HashSet<String>();
    return Optional.of(ctx.model())
        .map(ApiWiserBundle.CodeModel::vars)
        .stream()
        .flatMap(Collection::stream)
        .map(v -> fieldSpec(ctx.toBuilder().reservedFieldNames(reservedFieldNames).property(v).build()));
  }

  static FieldSpec fieldSpec(final JacksonModelBuilder ctx) {
    return ctx.fieldSpec();
  }
}
