package org.metalib.wiser.api.core.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;

import java.util.List;
import java.util.Map;

/**
 * Represents a context for API Wiser operations.
 * This class holds information about Maven dependencies organized by category.
 * It is typically created by the {@link ApiWiserModel#apiWiserContext(Map)} method.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiWiserContext {
    /**
     * A map of dependencies where the key is a category or module name
     * and the value is a list of Maven dependencies for that category.
     */
    Map<String, List<ApiWiserMavenDependency>> dependencies;
}
