package org.metalib.wiser.api.template.maven.model;

import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class MvnDependencies {

    final List<Dependency> list;
    final Map<String, Dependency> map = new HashMap<>();
    boolean stripVersion = false;

    public static MvnDependencies create() {
        return new MvnDependencies(new ArrayList<>());
    }

    public static MvnDependencies wrap(List<Dependency> list) {
        final var result = new MvnDependencies(new ArrayList<>(list));
        list.forEach(dep -> result.map.put(key(dep.getGroupId(), dep.getArtifactId()), dep));
        return result;
    }

    public MvnDependencies withVersionStripped() {
        stripVersion = true;
        return this;
    }

    public MvnDependencies withVersion() {
        stripVersion = false;
        return this;
    }

    public MvnDependencies add(Dependency dependency) {
        map.computeIfAbsent(
                key(dependency.getGroupId(), dependency.getArtifactId()),
                k -> {
                    final var d = copy(dependency);
                    if (stripVersion) {
                        d.setVersion(null);
                    }
                    list.add(d);
                    return d;
                }
        );
        return this;
    }

    public MvnDependencies addTest(Dependency dependency) {
        map.computeIfAbsent(
                key(dependency.getGroupId(), dependency.getArtifactId()),
                k -> {
                    final var d = copy(dependency);
                    if (stripVersion) {
                        d.setVersion(null);
                    }
                    d.setScope(MvnScope.TEST.toString());
                    list.add(d);
                    return dependency;
                }
        );
        return this;
    }

    public MvnDependencies add(String groupId, String artifactId) {
        map.computeIfAbsent(
                key(groupId, artifactId),
                k -> {
                    final var dependency = new Dependency();
                    dependency.setGroupId(groupId);
                    dependency.setArtifactId(artifactId);
                    list.add(dependency);
                    return dependency;
                }
        );
        return this;
    }

    public List<Dependency> list() {
        return list;
    }

    public static Dependency toDependency(String coords) {
        return Optional.ofNullable(coords).map(v -> {
            final var parts = v.split(":");
            final var dependency = new Dependency();

            if (parts.length == 0) {
                return dependency;
            }

            if (parts.length == 1) {
                // only artifactId is provided. We consider it from api-wiser family.
                dependency.setGroupId("org.metalib.api.wiser");
                dependency.setArtifactId(parts[0]);
                dependency.setVersion("${api-wiser.version}");
                return dependency;
            }

            dependency.setGroupId(parts[0]);
            dependency.setArtifactId(parts[1]);

            if (parts.length > 2) {
                dependency.setVersion(parts[2]);
            }
            if (parts.length > 3) {
                dependency.setScope(parts[3]);
            }
            return dependency;
        }).orElse(null);
    }

    private static String key(String groupId, String artifactId) {
        return groupId + ":" + artifactId;
    }

    static Dependency copy(Dependency dependency) {
        final var result = new Dependency();
        result.setGroupId(dependency.getGroupId());
        result.setArtifactId(dependency.getArtifactId());
        result.setVersion(dependency.getVersion());
        result.setScope(dependency.getScope());
        result.setType(dependency.getType());
        result.setClassifier(dependency.getClassifier());
        result.setSystemPath(dependency.getSystemPath());
        result.setOptional(dependency.isOptional());
        result.setExclusions(new ArrayList<>(dependency.getExclusions()));
        return result;
    }
}