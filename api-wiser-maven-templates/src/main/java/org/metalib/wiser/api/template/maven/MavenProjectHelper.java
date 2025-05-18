package org.metalib.wiser.api.template.maven;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.maven.model.Dependency;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.maven.model.MavenLib;
import org.metalib.wiser.api.template.maven.model.PomModel;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.String.format;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;
import static org.metalib.wiser.api.template.maven.model.MavenLib.JACKSON_BOM;
import static org.metalib.wiser.api.template.maven.model.MavenLib.JERSEY_URL_BUILDER;
import static org.metalib.wiser.api.template.maven.model.MavenLib.JUNIT4;
import static org.metalib.wiser.api.template.maven.model.MavenLib.JUNIT5_BOM;
import static org.metalib.wiser.api.template.maven.model.MavenLib.LOMBOK;
import static org.metalib.wiser.api.template.maven.model.MavenLib.MAPSTRUCT;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_BOM;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_FRAMEWORK_BOM;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MavenProjectHelper {

    static final String X_API_WISER_MAVEN_MODEL = "x" + DASH + API_WISER + DASH + "maven-model";
    static final String X_API_WISER_MAVEN_BUILD = "x" + DASH + API_WISER + DASH + "maven-build";
    static final String X_API_WISER_MAVEN_BUILD_ROOT = X_API_WISER_MAVEN_BUILD + "-root";
    static final String X_API_WISER_MAVEN_BUILD_ROOT_PARENT = X_API_WISER_MAVEN_BUILD_ROOT + "-parent";
    static final String X_API_WISER_PROJECT_DIR = 'x' + DASH + API_WISER + DASH + "project-dir";

    public static final String POM_XML = POM + DOT + XML;
    public static final String ORG_JUNIT_MAVEN_GROUP = "org.junit";
    public static final String IMPORT = "import";
    public static final String VERSION = "version";
    public static final String DOT_VERSION = DOT + VERSION;
    public static final Map<MavenLib, String> MAVEN_DEPENDENCY_VERSIONS = Map.of(
            LOMBOK, "1.18.32",
            MAPSTRUCT, "1.5.5.Final",
            JACKSON_BOM, "2.14.2",
            SPRING_FRAMEWORK_BOM, "5.3.29",
            SPRING_BOOT_BOM, "2.7.14",
            JERSEY_URL_BUILDER, "0.0.1",
            JUNIT4, "4.13.2",
            JUNIT5_BOM, "5.10.1");


    public static Dependency[] dependencies(Map<String, String> dependencyVersions) {
        final var result = new ArrayList<Dependency>();
        dependencyVersions.forEach((k,v) -> {
            // dependency key format: <group id>:<artifact id>:<scope>:<type>
            // <scope> amd <type> are optional
            final var subs = k.split(COLON);
            if (0 == subs.length) {
                throw new DependencyParsingException(format("Group Id is not specified in '%s'", k));
            }
            final var dependency = new Dependency();
            dependency.setGroupId(subs[0]);
            if (1 == subs.length) {
                throw new DependencyParsingException(format("Artifact Id is not specified in '%s'", k));
            }
            dependency.setArtifactId(subs[1]);
            if (2 < subs.length) {
                dependency.setScope(subs[2]);
            }
            if (3 < subs.length) {
                dependency.setType(subs[3]);
            }
            dependency.setVersion(v);
            result.add(dependency);
        });
        return result.toArray(new Dependency[0]);
    }

    public static PomModel toPomModelBuilder(ApiWiserBundle bundle) {
        return PomModel
                .builder()
                .groupId(bundle.groupId())
                .artifactId(bundle.artifactId())
                .version(bundle.artifactVersion())
                .targetFile(bundle.targetFile())
                .build();
    }

    public static class DependencyParsingException extends RuntimeException {
        DependencyParsingException(String message) {
            super(message);
        }
    }
}
