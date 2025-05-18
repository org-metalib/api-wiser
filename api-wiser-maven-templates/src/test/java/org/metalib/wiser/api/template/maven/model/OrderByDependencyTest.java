package org.metalib.wiser.api.template.maven.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.metalib.wiser.api.template.maven.model.MavenModule.orderByDependency;

class OrderByDependencyTest {

    @Test
    void test0() {
        final var moduleMap = new HashMap<String, Set<String>>();
        moduleMap.put("c", Set.of(""));
        moduleMap.put("b", Set.of("c"));
        moduleMap.put("", Set.of());
        moduleMap.put("a", Set.of("b"));
        final var result = orderByDependency(moduleMap);
        assertTrue(result.indexOf("c") < result.indexOf("b"));
        assertTrue(result.indexOf("b") < result.indexOf("a"));
    }

    @Test
    void test1() {
        final var moduleMap = new HashMap<String, Set<String>>();
        moduleMap.put("c", Set.of(""));
        moduleMap.put("b", Set.of("c"));
        moduleMap.put("", Set.of());
        moduleMap.put("a", Set.of("c"));
        final var result = orderByDependency(moduleMap);
        assertTrue(result.indexOf("c") < result.indexOf("b"));
        assertTrue(result.indexOf("c") < result.indexOf("a"));
    }

    @Test
    void test2() {
        final var moduleMap = new HashMap<String, Set<String>>();
        moduleMap.put("c", Set.of("a"));
        moduleMap.put("b", Set.of("c"));
        moduleMap.put("", Set.of());
        moduleMap.put("a", Set.of("c"));
        assertThrows(MavenModule.CircularDependencyException.class, () -> orderByDependency(moduleMap));
    }

    @Test
    void test3() {
        final var moduleMap = new HashMap<String, Set<String>>();
        moduleMap.put("c", Set.of("a"));
        moduleMap.put("b", Set.of("c"));
        moduleMap.put("", Set.of());
        moduleMap.put("a", Set.of("b"));
        assertThrows(MavenModule.CircularDependencyException.class, () -> orderByDependency(moduleMap));
    }
}
