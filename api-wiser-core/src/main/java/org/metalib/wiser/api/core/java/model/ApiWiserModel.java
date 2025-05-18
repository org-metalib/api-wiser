package org.metalib.wiser.api.core.java.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiWiserModel {

    static ObjectMapper mapper = new ObjectMapper();

    public static ApiWiserPath apiWiserPath(Map<String, Object> value) {
        return mapper.convertValue(value, ApiWiserPath.class);
    }

    public static ApiWiserContext apiWiserContext(Map<String, Object> value) {
        final var result = ApiWiserContext.builder();
        final var dependency = value.get("dependencies");
        if (dependency instanceof Map) {
            final var dependencyMap = new LinkedHashMap<String, List<ApiWiserMavenDependency>>();
            for (final var entry : ((Map<String, List<String>>) dependency).entrySet()) {
                final var dependencies = entry.getValue();
                if (null == dependencies) {
                    continue;
                }
                final var a = dependencies.stream()
                        .map(ApiWiserMavenDependency::parse)
                        .collect(Collectors.toList());
                dependencyMap.put(entry.getKey(), a);
            }
            result.dependencies(dependencyMap);
        } else {
            result.dependencies(Map.of());
        }
        return result.build();
    }
}
