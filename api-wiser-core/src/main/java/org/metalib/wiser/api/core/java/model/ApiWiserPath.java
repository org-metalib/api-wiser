package org.metalib.wiser.api.core.java.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Represents a path in the API Wiser system with context information for different modules.
 * This class is typically created by the {@link ApiWiserModel#apiWiserPath(Map)} method.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiWiserPath {
    /**
     * A map of module contexts where the key is a module name or identifier
     * and the value is the context information for that module.
     */
    Map<String, ModuleContext> context;

    /**
     * Represents the context for a specific module, containing imports and properties.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class ModuleContext {
        /**
         * List of import statements required by the module.
         */
        List<String> imports;

        /**
         * List of fields or properties defined in the module.
         */
        List<Field> properties;
    }

    /**
     * Represents a field or property with a name and type.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Field {
        /**
         * The name of the field.
         */
        String name;

        /**
         * The data type of the field.
         */
        String type;
    }
}
