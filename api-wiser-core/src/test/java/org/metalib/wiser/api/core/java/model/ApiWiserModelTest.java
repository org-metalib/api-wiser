package org.metalib.wiser.api.core.java.model;

import org.junit.jupiter.api.Test;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;
import org.metalib.wiser.api.template.MavenDependency;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiWiserModelTest {

    @Test
    void test0() {
        final var result = ApiWiserModel.apiWiserPath(Map.of(
                "context", Map.of("biz",
                        Map.of("imports", List.of("org.metalib.Service"),
                                "properties", List.of(Map.of("name", "service", "type", "Service"))))));
        assertNotNull(result);
    }

    @Test
    void test1() {
        final var result = ApiWiserModel.apiWiserContext(Map.of(
                "dependencies", Map.of("biz", List.of("org.metalib.api.wiser:api-wiser-core:0.0.1"))));
        assertNotNull(result);
        assertNotNull(result.getDependencies());
        assertNotNull(result.getDependencies().get("biz"));
        assertEquals(1, result.getDependencies().get("biz").size());
        assertEquals(ApiWiserMavenDependency.builder()
                        .groupId("org.metalib.api.wiser")
                        .artifactId("api-wiser-core")
                        .version("0.0.1")
                        .build(),
                result.getDependencies().get("biz").get(0));
    }

    @Test
    void test2() {
        final var result = ApiWiserMavenDependency.toList(Collections.singletonList(ApiWiserMavenDependency.builder().build()));
        final MavenDependency[] a = result.toArray(new MavenDependency[0]);
        return;
    }
}