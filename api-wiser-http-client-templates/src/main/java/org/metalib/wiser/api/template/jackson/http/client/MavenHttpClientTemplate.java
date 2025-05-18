package org.metalib.wiser.api.template.jackson.http.client;

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
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.MODULE_NAME;

public class MavenHttpClientTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "maven-http-client";
    public static final String TEMPLATE_ID = API_WISER + DOUBLE_COLON + TEMPLATE_NAME;

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
        return new String[]{API};
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
                .dependency("org.metalib.net.url", "jersey-url-builder")
                .dependency("org.junit.jupiter", "junit-jupiter-engine", MavenScope.TEST)
                .toXml();
    }

    static Dependency toDependency(ApiWiserBundle bundle, String moduleName) {
        final var result = new Dependency();
        result.setGroupId(bundle.groupId());
        result.setArtifactId(bundle.artifactId() + DASH + moduleName);
        return result;
    }
}
