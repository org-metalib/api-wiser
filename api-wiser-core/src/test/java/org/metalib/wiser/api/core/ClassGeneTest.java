package org.metalib.wiser.api.core;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.Modifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

class ClassGeneTest {
  @Test
  void test() {
    final var fields = List.of("clazz", "name", "id");
    final var javaFileBuilder = JavaFile.builder("org.metalib.java.poet",
        TypeSpec.classBuilder("JavaBean")
            .addAnnotation(Data.class)
            .addAnnotation(AllArgsConstructor.class)
            .addAnnotation(NoArgsConstructor.class)
            .addAnnotation(AnnotationSpec.builder(Builder.class)
                .addMember("toBuilder", "$L", "true")
                .build())
            .addAnnotation(AnnotationSpec.builder(JsonPropertyOrder.class)
                .addMember("value", "{" + fields
                    .stream()
                    .map(v -> '"' + v + '"')
                    .collect(joining(",")) + "}")
                .build())
            .addModifiers(Modifier.PUBLIC)
            .build());
    final var result = javaFileBuilder.build().toString();
    return;

  }

  @Test
  void testEnumClass() throws JsonProcessingException {
    final var template = "{\"val\":\"_abc\"}";
    final var jackson = new ObjectMapper();
    final var bean = jackson.readValue(template, Bean.class);
    final var json = jackson.writeValueAsString(bean);
    assertEquals(EnumClass._ABC, EnumClass._ABC.fromValue(EnumClass._ABC.getValue()));
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder()
  static class Bean {
    EnumClass val;
  }

  @Getter
  @RequiredArgsConstructor
  public enum EnumClass {
    _ABC("_abc"),

    _EFG("-efg"),

    _XYZ_("(xyz)");

    @JsonValue
    private final String value;

    @JsonCreator
    public static EnumClass fromValue(final String value) {
      return Arrays.stream(values())
          .filter(v -> v.value.equals(value))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("Unexpected value '" + value + "'"));
    }
  }

}
