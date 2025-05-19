package org.metalib.wiser.api.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the {@link MavenDependency} interface representing a Maven dependency
 * in the API Wiser system. This class provides functionality for parsing dependency strings
 * and comparing dependencies.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiWiserMavenDependency implements MavenDependency<ApiWiserMavenDependency> {

    /**
     * The group ID of the dependency.
     */
    String groupId;

    /**
     * The artifact ID of the dependency.
     */
    String artifactId;

    /**
     * The version of the dependency.
     */
    String version;

    /**
     * The scope of the dependency (e.g., compile, test, provided).
     */
    String scope;

    /**
     * The type of the dependency.
     */
    String type;

    /**
     * The Bill of Materials (BOM) dependency associated with this dependency.
     */
    ApiWiserMavenDependency bom;

    /**
     * Compares this dependency with another dependency based on their group ID and artifact ID.
     * If either dependency has a null group ID or artifact ID, they are considered equal.
     *
     * @param o The dependency to compare with
     * @return A negative integer, zero, or a positive integer as this dependency is less than,
     *         equal to, or greater than the specified dependency
     */
    @Override
    public int compareTo(ApiWiserMavenDependency o) {
        if (Objects.isNull(o)) {
            return 1;
        }
        if (Objects.isNull(groupId) || Objects.isNull(o.groupId) || Objects.isNull(artifactId) || Objects.isNull(o.artifactId)) {
            return 0;
        }
        int group = Objects.compare(groupId, o.groupId, String::compareTo);
        return 0 == group ? Objects.compare(artifactId, o.artifactId, String::compareTo) : group;
    }

    /**
     * Parses a colon-separated string into an ApiWiserMavenDependency object.
     * The string format is expected to be: "groupId:artifactId:version:scope:type".
     * Each part is optional, but they must appear in this order.
     *
     * @param dependency The dependency string to parse
     * @return An ApiWiserMavenDependency object created from the parsed string
     */
    public static ApiWiserMavenDependency parse(String dependency) {
        final var result = ApiWiserMavenDependency.builder();
        final var parts = dependency.split(":");
        if (parts.length > 0) {
            result.groupId(parts[0]);
        }
        if (parts.length > 1) {
            result.artifactId(parts[1]);
        }
        if (parts.length > 2) {
            result.version(parts[2]);
        }
        if (parts.length > 3) {
            result.scope(parts[3]);
        }
        if (parts.length > 4) {
            result.type(parts[4]);
        }
        return result.build();
    }

    /**
     * Converts a list of ApiWiserMavenDependency objects to a list of MavenDependency objects.
     *
     * @param dependency The list of ApiWiserMavenDependency objects to convert
     * @return A list of MavenDependency objects
     */
    public static List<MavenDependency<?>> toList(List<ApiWiserMavenDependency> dependency) {
        return new ArrayList<>(dependency);
    }
}
