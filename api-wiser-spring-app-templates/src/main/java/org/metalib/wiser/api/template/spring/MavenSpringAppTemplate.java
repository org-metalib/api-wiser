package org.metalib.wiser.api.template.spring;

import org.apache.maven.model.Dependency;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserFinals;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.template.maven.model.MavenScope;
import org.metalib.wiser.api.template.maven.model.PomModel;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.BIZ;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_STARTER_TEST;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_STARTER_WEB;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.DASH;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.MODULE_NAME;

public class MavenSpringAppTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "spring-app-pom";
    public static final String TEMPLATE_ID = API_WISER + DOUBLE_COLON + TEMPLATE_NAME;
    static final String POM = "pom";
    static final String XML = "xml";

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    @Override
    public String[] moduleDependencies() {
        return new String[]{BIZ};
    }

    @Override
    public boolean isSupportingFile() {
        return true;
    }

    @Override
    public String fileExtension() {
        return XML;
    }

    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId() + DASH + MODULE_NAME;
            }

            @Override
            public String fileName() {
                return POM;
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        return PomModel.builder()
                .parentGroupId(bundle.groupId())
                .parentArtifactId(bundle.artifactId())
                .parentVersion(bundle.artifactVersion())
                .artifactId(bundle.artifactId() + DASH + MODULE_NAME)
                .build()
                .property(ApiWiserFinals.moduleNameProperty(), moduleName())
                .dependencies(stream(moduleDependencies())
                        .map(v -> toDependency(bundle, v))
                        .collect(toList()).toArray(Dependency[]::new))
                .dependency(SPRING_BOOT_STARTER_WEB)
                .dependency(SPRING_BOOT_STARTER_TEST, MavenScope.TEST)
                .toXml();
    }

    static Dependency toDependency(ApiWiserBundle bundle, String moduleName) {
        final var result = new Dependency();
        result.setGroupId(bundle.groupId());
        result.setArtifactId(bundle.artifactId() + DASH + moduleName);
        return result;
    }
}
