# API Wiser JavaPoet

## Overview

The `api-wiser-javapoet` module introduces `TypeRefInfo`, a robust utility class designed to simplify the parsing, resolution, and representation of Java types. It excels at converting type definitions from strings (like `List<Map<String, Integer>>` or `int[]`) into corresponding JavaPoet `TypeName` objects. This makes it an invaluable tool for code generation, metaprogramming, or any scenario where you need to dynamically work with Java type structures.

`TypeRefInfo` intelligently handles various type constructs, including:
*   Primitive types (e.g., `int`, `boolean`)
*   Class types (e.g., `java.lang.String`, `com.example.MyClass`)
*   Parameterized types (e.g., `List<String>`, `Map<K, V>`)
*   Nested generic types (e.g., `List<Map<String, Set<Integer>>>`)
*   Array types (e.g., `String[]`, `int[][]`, `List<String>[]`)

It leverages the powerful JavaPoet library for generating `.java` source files, and `TypeRefInfo` acts as a bridge, making it easier to construct the necessary `TypeName` instances for JavaPoet.

## Motivation

Manually constructing complex JavaPoet `TypeName` objects, especially for deeply nested generics or arrays of generics, can be verbose, cumbersome, and prone to errors. `TypeRefInfo` addresses this by:

*   **Simplifying Type Definition:** Allows developers to define types using familiar Java syntax in string format.
*   **Reducing Boilerplate:** Automates the conversion from string representations to JavaPoet `TypeName` objects.
*   **Improving Readability:** Makes code that deals with dynamic type creation cleaner and easier to understand.
*   **Enhancing Accuracy:** Includes validation for type definitions, such as ensuring proper bracket matching for generics.

## Key Features

* **Comprehensive Type String Parsing:**
  * Accurately parses type strings representing primitive types, class/interface names, parameterized types, and array types.
  * Supports fully qualified class names (e.g., `java.util.List`) and simple class names (e.g., `List`) when accompanied by a set of import statements for resolution.
  * Correctly interprets nested generics (e.g., `Outer<Inner<Another>>`).
  * Handles multi-dimensional arrays and arrays of generic types (e.g., `Map<String, Integer>[][]`).
* **Intelligent Import Resolution:**
  *   Utilizes a provided set of import strings (e.g., `java.util.List`, `com.example.CustomType`) to resolve simple names to their fully qualified counterparts.
  *   Automatically assumes `java.lang.*` imports for common types like `String`, `Integer`, etc., if not explicitly provided.
* **Seamless JavaPoet `TypeName` Conversion:**
  * Converts the parsed type information directly into the appropriate JavaPoet `TypeName` subclass:
    *   `com.squareup.javapoet.ClassName` for class types.
    *   `com.squareup.javapoet.ParameterizedTypeName` for generic types.
    *   `com.squareup.javapoet.ArrayTypeName` for array types.
    *   Primitive `com.squareup.javapoet.TypeName` constants (e.g., `TypeName.INT`, `TypeName.BOOLEAN`).
*   **Validation:**
  *   Performs basic validation on the type string, such as checking for balanced generic angle brackets (`< >`).

## Example Usage

```java
import org.metalib.wiser.api.javapoet.TypeRefInfo;

import java.util.Set;

public class Example {
    public static void main(String[] args) {
        String type = "List<Map<String, Integer>>";
        Set<String> imports = Set.of("java.util.List", "java.util.Map");

        TypeRefInfo typeRefInfo = TypeRefInfo.parse(type, imports);
        System.out.println(typeRefInfo.toTypeName());
    }
}
```

Error `HandlingTypeRefInfo.parse()` may throw exceptions for invalid input:
* `IllegalArgumentException`:
  * If the type string is null, empty, or malformed (e.g., mismatched generic brackets < >).
  * If a simple class name cannot be resolved using the provided imports or the default `java.lang.*` package.
  * For other parsing inconsistencies.
 
It's recommended to wrap calls to parse() in a try-catch block if you are dealing with potentially unreliable type strings.