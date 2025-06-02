package org.metalib.wiser.api.javapoet;

import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Primitive, class and parameterized class info resolution
 */
@Value
@Builder(toBuilder = true)
@Accessors(fluent = true)
public class TypeRefInfo {

  /**
   * java primitive types
   * This map is used to resolve and refer to primitive types, including `void`
   * within the context of the `TypeRefInfo` class.
   */
  static Map<String, TypeName> primitiveTypeNames = Map.of(
      "void", TypeName.VOID,
      "boolean", TypeName.BOOLEAN,
      "byte", TypeName.BYTE,
      "short", TypeName.SHORT,
      "int",  TypeName.INT,
      "long",  TypeName.LONG,
      "char",  TypeName.CHAR,
      "float", TypeName.FLOAT,
      "double", TypeName.DOUBLE);

  String type;
  List<TypeRefInfo> enclosedTypes;
  boolean isArray;

  /**
   * Parses a type string into a `TypeRefInfo` object. Supports generics and arrays.
   * @param type java type
   * @param imports import set
   * @return TypeRefInfo instance
   */
  public static TypeRefInfo parse(String type, Set<String> imports) {
    if (null == type || type.isBlank()) {
      return TypeRefInfo.builder().type("void").build();
    }
    final var isArray = type.endsWith("[]");
    final var index = type.indexOf('<');
    final var rindex = type.lastIndexOf('>');
    if (index+1 == type.length()) {
      throw new TypeRefInfoException(format("Enclosed type(s) brackets mismatch in generic type definition: %s", type));
    }
    if (-1 == index) {
      return TypeRefInfo.builder().type(importCheck(type, imports)).isArray(isArray).build();
    }
    if (-1 == rindex) {
      throw new TypeRefInfoException(format("Enclosed type(s) brackets mismatch in generic type definition: %s", type));
    }
    return TypeRefInfo.builder()
        .type(importCheck(type.substring(0, index), imports))
        .isArray(isArray)
        .enclosedTypes(parseTypeList(type.substring(index+1, rindex))
            .stream()
            .map(v -> parse(v, imports))
            .toList())
        .build();
  }

  /**
   * Checks if a type exists in the provided imports and resolves it to its fully qualified name if necessary.
   * @param type java type
   * @param imports import set
   * @return String
   */
  static String importCheck(final String type, final Set<String> imports) {
    final var typeTail = '.' + type;
    return imports.stream().filter(v -> v.endsWith(typeTail)).findFirst().orElse(type);
  }

  /**
   * Parses a comma-separated list of types (e.g., `Map&lt;String, List&lt;Integer&gt;&gt;`) into individual type strings.
   * @param typeList type list
   * @return List&lt;String&gt; instance
   */
  public static List<String> parseTypeList(String typeList) {
    // we don't parse comments although we most likely should.
    final var result = new ArrayList<String>();
    int level = 0;
    final var buffer = new StringBuilder();
    for (final var ch : typeList.toCharArray()) {
      switch (ch) {
        case '<':
          level++;
          buffer.append(ch);
          break;
        case '>':
          level--;
          buffer.append(ch);
          break;
        case ',':
          if (0 == level) {
            result.add(buffer.toString().trim());
            buffer.setLength(0);
          } else {
            buffer.append(ch);
          }
          break;
        default:
          buffer.append(ch);
      }
    }
    if (0 != level) {
      throw new TypeRefInfoException(format("Generic type open/close bracket mismatch for %s", typeList));
    }
    if (!buffer.isEmpty()) {
      result.add(buffer.toString().trim());
    }
    return result;
  }

  /**
   * Converts the `TypeRefInfo` object into a JavaPoet `TypeName` representation.
   * @return TypeName instance
   */
  public TypeName toTypeName() {
    final var typeBase = isArray ? type.substring(0, type.length() - 2) : type;
    final var typeParams = Optional.ofNullable(enclosedTypes).stream()
        .flatMap(Collection::stream)
        .map(TypeRefInfo::toTypeName)
        .toArray(TypeName[]::new);
    final var classBase = Optional.of(typeBase)
        .map(v -> primitiveTypeNames.get(v))
        .orElseGet(() -> ClassName.bestGuess(typeBase));
    final var resultBase = 0 == typeParams.length
        ? classBase
        : ParameterizedTypeName.get((ClassName) classBase, typeParams);
    return isArray ? ArrayTypeName.of(resultBase) : resultBase;
  }

  /**
   * Represents an exception thrown during the parsing or handling of type references
   * within the TypeRefInfo class.
   */
  public static class TypeRefInfoException extends RuntimeException {
    TypeRefInfoException(String message) {
      super(message);
    }
  }
}
