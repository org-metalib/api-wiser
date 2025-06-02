package org.metalib.wiser.api.core.java.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for creating and manipulating API Wiser model objects.
 * This class provides methods to convert maps to specific API Wiser model objects.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiWiserModel {

    /** Jackson ObjectMapper used for converting between maps and model objects */
    static ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts a map to an ApiWiserPath object.
     *
     * @param value The map containing path information to convert
     * @return An ApiWiserPath object created from the provided map
     */
    public static ApiWiserPath apiWiserPath(Map<String, Object> value) {
        return mapper.convertValue(value, ApiWiserPath.class);
    }

    /**
     * Creates an ApiWiserContext object from a map, specifically handling dependencies.
     * This method extracts the "dependencies" entry from the provided map and converts
     * it to a map of ApiWiserMavenDependency lists.
     *
     * @param value The map containing context information, including dependencies
     * @return An ApiWiserContext object with the dependencies from the provided map
     */
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
                dependencyMap.put(entry.getKey(), dependencies.stream().map(ApiWiserMavenDependency::parse).toList());
            }
            result.dependencies(dependencyMap);
        } else {
            result.dependencies(Map.of());
        }
        return result.build();
    }
}
