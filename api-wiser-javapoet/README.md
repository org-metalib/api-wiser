# API Wiser Java Poet

## Overview

The `TypeRefInfo` class is a utility for resolving and representing type information in Java. It supports primitive types, class types, parameterized types, and array types. This class is part of the `api-wiser-javapoet` module and leverages the JavaPoet library for type representation.

## Key Features

### Type Parsing
- Parses type strings (e.g., `List<String>`, `int[]`) into structured representations.
- Handles generic types and nested generic types.
- Validates type definitions, ensuring proper bracket matching for generics.

### Type Conversion
- Converts parsed type information into JavaPoet's `TypeName` objects, supporting:
    - Primitive types
    - Class types
    - Parameterized types
    - Array types

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