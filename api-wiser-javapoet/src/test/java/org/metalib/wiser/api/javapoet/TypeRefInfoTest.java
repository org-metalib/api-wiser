package org.metalib.wiser.api.javapoet;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TypeRefInfoTest {
    private static final Set<String> EMPTY_IMPORTS = Set.of();
    private static final Set<String> COMMON_IMPORTS = Set.of(
            "java.util.List",
            "java.util.Map",
            "java.lang.String",
            "java.lang.Integer"
    );

    private void assertTypeRefInfo(TypeRefInfo info, String expectedType, boolean isArray) {
        assertNotNull(info);
        assertEquals(expectedType, info.type());
        assertEquals(isArray, info.isArray());
    }

    @Nested
    class BasicTypeTests {
        @Test
        void shouldParsePrimitiveType() {
            var parsed = TypeRefInfo.parse("int", EMPTY_IMPORTS);
            assertTypeRefInfo(parsed, "int", false);
            assertTrue(Optional.of(parsed).map(TypeRefInfo::enclosedTypes).isEmpty());
        }

        @Test
        void shouldParseArrayType() {
            var parsed = TypeRefInfo.parse("String[]", EMPTY_IMPORTS);
            assertTypeRefInfo(parsed, "String[]", true);
            assertTrue(Optional.of(parsed).map(TypeRefInfo::enclosedTypes).isEmpty());
        }

        @Test
        void shouldHandleNullAndEmptyTypes() {
            assertTypeRefInfo(TypeRefInfo.parse(null, EMPTY_IMPORTS), "void", false);
            assertTypeRefInfo(TypeRefInfo.parse("  ", EMPTY_IMPORTS), "void", false);
            assertTypeRefInfo(TypeRefInfo.parse(null, null), "void", false);
        }
    }

    @Nested
    class GenericTypeTests {
        @Test
        void shouldParseGenericArrayType() {
            var info = TypeRefInfo.parse("List<String>[]", Set.of("java.util.List"));
            assertTypeRefInfo(info, "java.util.List", true);
            assertEquals(1, info.enclosedTypes().size());
            assertEquals("String", info.enclosedTypes().get(0).type());
        }

        @Test
        void shouldParseNestedGenericsWithArrays() {
            var info = TypeRefInfo.parse("Map<String[], List<Integer[]>>",
                    Set.of("java.util.Map", "java.util.List"));

            assertTypeRefInfo(info, "java.util.Map", false);
            assertEquals(2, info.enclosedTypes().size());

            var firstType = info.enclosedTypes().get(0);
            assertTypeRefInfo(firstType, "String[]", true);

            var secondType = info.enclosedTypes().get(1);
            assertTypeRefInfo(secondType, "java.util.List", false);
            assertEquals("Integer[]", secondType.enclosedTypes().get(0).type());
            assertTrue(secondType.enclosedTypes().get(0).isArray());
        }
    }

    @Nested
    class TypeNameTests {
        @Test
        void shouldGenerateCorrectTypeNames() {
            assertEquals("int",
                    TypeRefInfo.parse("int", EMPTY_IMPORTS).toTypeName().toString());

            assertEquals("java.lang.String",
                    TypeRefInfo.parse("String", Set.of("java.lang.String")).toTypeName().toString());

            assertEquals("java.util.List<java.lang.String>",
                    TypeRefInfo.parse("List<String>", COMMON_IMPORTS).toTypeName().toString());

            assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>",
                    TypeRefInfo.parse("Map<String, List<Integer>>", COMMON_IMPORTS).toTypeName().toString());
        }
    }

    @Nested
    class ErrorHandlingTests {
        @Test
        void shouldThrowExceptionForInvalidSyntax() {
            assertThrows(TypeRefInfo.TypeRefInfoException.class,
                    () -> TypeRefInfo.parse("<String", EMPTY_IMPORTS));
            assertThrows(TypeRefInfo.TypeRefInfoException.class,
                    () -> TypeRefInfo.parse("<", EMPTY_IMPORTS));
            assertThrows(TypeRefInfo.TypeRefInfoException.class,
                    () -> TypeRefInfo.parseTypeList(">"));
            assertThrows(TypeRefInfo.TypeRefInfoException.class,
                    () -> TypeRefInfo.parseTypeList("<"));
        }
    }

    @Nested
    class TypeListTests {
        @Test
        void shouldParseTypeList() {
            assertTrue(TypeRefInfo.parseTypeList("").isEmpty());

            var types = new String[]{"Map", "List", "Array"};
            var parsed = TypeRefInfo.parseTypeList(String.join(",", types));
            assertEquals(types.length, parsed.size());
            for (int i = 0; i < types.length; i++) {
                assertEquals(types[i], parsed.get(i));
            }
        }

        @Test
        void shouldParseComplexTypeList() {
            var complexTypes = new String[]{
                    "String",
                    "List<String>",
                    "Map<String,*>",
                    "Map<String, List< String>>"
            };
            var parsed = TypeRefInfo.parseTypeList(String.join(",", complexTypes));
            assertEquals(complexTypes.length, parsed.size());
            for (int i = 0; i < complexTypes.length; i++) {
                assertEquals(complexTypes[i], parsed.get(i));
            }
        }
    }

    @Test
    void shouldParseEmptyGenericType() {
        var info = TypeRefInfo.parse("List<>", Set.of("java.util.List"));
        assertTypeRefInfo(info, "java.util.List", false);
        assertTrue(info.enclosedTypes().isEmpty());
    }

    @Test
    void shouldParseWildcardGenericType() {
        var info = TypeRefInfo.parse("List<?>", Set.of("java.util.List"));
        assertTypeRefInfo(info, "java.util.List", false);
        assertEquals(1, info.enclosedTypes().size());
        assertEquals("?", info.enclosedTypes().get(0).type());
    }

    @Test
    void shouldParseMultiDimensionalArrays() {
        var info = TypeRefInfo.parse("String[][]", EMPTY_IMPORTS);
        assertTypeRefInfo(info, "String[][]", true);
        assertTrue(Optional.of(info).map(TypeRefInfo::enclosedTypes).isEmpty());
    }

    @Test
    void shouldParseFullyQualifiedNames() {
        var info = TypeRefInfo.parse("java.util.List<java.lang.String>", EMPTY_IMPORTS);
        assertTypeRefInfo(info, "java.util.List", false);
        assertEquals(1, info.enclosedTypes().size());
        assertEquals("java.lang.String", info.enclosedTypes().get(0).type());
    }

    @Test
    void shouldThrowExceptionForMalformedGenerics() {
        assertThrows(TypeRefInfo.TypeRefInfoException.class,
                () -> TypeRefInfo.parse("List<String", COMMON_IMPORTS));
        assertThrows(TypeRefInfo.TypeRefInfoException.class,
                () -> TypeRefInfo.parse("List<<String>", COMMON_IMPORTS));
        assertThrows(TypeRefInfo.TypeRefInfoException.class,
                () -> TypeRefInfo.parse("List<String>>", COMMON_IMPORTS));
    }

    @Test
    void shouldHandleWhitespaceInTypeList() {
        var parsed = TypeRefInfo.parseTypeList("String , List<Integer> , Map<String,List<String>>");
        assertEquals(3, parsed.size());
        assertEquals("String", parsed.get(0));
        assertEquals("List<Integer>", parsed.get(1));
        assertEquals("Map<String,List<String>>", parsed.get(2));
    }

    @Test
    void shouldHandleNestedCommasInTypeList() {
        var parsed = TypeRefInfo.parseTypeList("Map<String,Integer>,List<String>");
        assertEquals(2, parsed.size());
        assertEquals("Map<String,Integer>", parsed.get(0));
        assertEquals("List<String>", parsed.get(1));
    }

    @Test
    void shouldResolveTypesWithImports() {
        var imports = Set.of(
                "com.example.CustomType",
                "com.example.Generic"
        );

        var info = TypeRefInfo.parse("Generic<CustomType>", imports);
        assertTypeRefInfo(info, "com.example.Generic", false);
        assertEquals(1, info.enclosedTypes().size());
        assertEquals("com.example.CustomType", info.enclosedTypes().get(0).type());
    }
}