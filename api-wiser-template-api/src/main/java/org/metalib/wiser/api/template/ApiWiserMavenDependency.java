package org.metalib.wiser.api.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ApiWiserMavenDependency implements MavenDependency<ApiWiserMavenDependency> {

    String groupId;
    String artifactId;
    String version;
    String scope;
    String type;
    ApiWiserMavenDependency bom;

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

    public static List<MavenDependency<?>> toList(List<ApiWiserMavenDependency> dependency) {
        return new ArrayList<>(dependency);
    }
}
