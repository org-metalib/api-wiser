package org.metalib.wiser.api.template.model;

import org.metalib.wiser.api.template.MavenDependency;

import java.util.List;
import java.util.Map;

/**
 * Dependency info for API Generators and Module generators
 */
public interface ApiWiserContext {

    ApiWiserContext EMPTY = new ApiWiserContext() {
        @Override
        public Map<String, List<MavenDependency>> dependencies() {
            return Map.of();
        }
    };

    Map<String, List<MavenDependency>> dependencies();
}
