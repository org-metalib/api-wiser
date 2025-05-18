package org.metalib.wiser.api.template.maven.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Dependency;
import org.metalib.wiser.api.template.MavenDependency;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.metalib.wiser.api.template.ApiWiserFinals.COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.ORG_JUNIT_MAVEN_GROUP;

@Getter
@RequiredArgsConstructor
public enum MavenLib implements MavenDependency<MavenLib> {
    LOMBOK("org.projectlombok", "lombok", "1.18.32"),
    MAPSTRUCT("org.mapstruct", "mapstruct", "1.5.5.Final"),
    JACKSON_BOM("com.fasterxml.jackson", "jackson-bom", "2.17.1"),
    JACKSON_ANNOTATION("com.fasterxml.jackson.core", "jackson-annotations", "2.17.1", JACKSON_BOM),
    JACKSON_DATABIND("com.fasterxml.jackson.core", "jackson-databind", "2.17.1", JACKSON_BOM),
    SPRING_FRAMEWORK_BOM("org.springframework", "spring-framework-bom", "5.3.35"),
    SPRING_BOOT_BOM("org.springframework.boot", "spring-boot-dependencies", "5.3.35"),
    SPRING_BOOT_STARTER_WEB("org.springframework.boot", "spring-boot-starter-web", "2.7.18", SPRING_BOOT_BOM),
    SPRING_BOOT_STARTER_TEST("org.springframework.boot", "spring-boot-starter-test", "2.7.18", SPRING_BOOT_BOM),
    JERSEY_URL_BUILDER("org.metalib.net.url", "jersey-url-builder", "0.0.1"),
    JUNIT4("junit", "junit", "4.13.2"),
    JUNIT5_BOM(ORG_JUNIT_MAVEN_GROUP, "junit-bom", "5.10.2"),
    JUNIT5_JUPITER_API(ORG_JUNIT_MAVEN_GROUP, "junit-jupiter-api", "5.10.2", JUNIT5_BOM),
    JUNIT5_JUPITER_ENGINE(ORG_JUNIT_MAVEN_GROUP, "junit-jupiter-engine", "5.10.2", JUNIT5_BOM),
    JUNIT5_JUPITER_PARAMS(ORG_JUNIT_MAVEN_GROUP, "junit-jupiter-params", "5.10.2", JUNIT5_BOM),
    ;
    static final Map<String, MavenLib> coordinateMap = new HashMap<>();

    static {
        Stream.of(MavenLib.values()).forEach(v -> coordinateMap
                .put(v.groupId + ':' + v.artifactId, v));
    }

    final String groupId;
    final String artifactId;
    final String version;
    final MavenLib bom;

    MavenLib(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, null);
    }

    public String versionPropertyName() {
        return name().replace('_', '-').toLowerCase() + ".version";
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getScope() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    public String coordId() {
        return this.groupId + COLON + artifactId;
    }

    public static String coordId(Dependency dependency) {
        return dependency.getGroupId() + COLON + dependency.getArtifactId();
    }

    public boolean isBOM() {
        return name().endsWith("_BOM");
    }

    public static Optional<MavenLib> of(Dependency dependency) {
        return Optional.ofNullable(coordinateMap.get(dependency.getGroupId() + COLON + dependency.getArtifactId()));
    }

    String linkedPropertyVersion() {
        return "${" + getArtifactId() + ".version}";
    }

    public Dependency toDependencyWithLinkedPropertyVersion() {
        return toDependency(linkedPropertyVersion());
    }

    public Dependency toDependency() {
        return toDependency(getVersion());
    }

    public Dependency toDependency(String version) {
        final var dependency = new Dependency();
        dependency.setGroupId(getGroupId());
        dependency.setArtifactId(getArtifactId());
        dependency.setVersion(version);
        dependency.setScope(getScope());
        if (isBOM()) {
            dependency.setType("pom");
            dependency.setScope("import");
        } else {
            dependency.setType(getType());
        }
        return dependency;
    }

}
