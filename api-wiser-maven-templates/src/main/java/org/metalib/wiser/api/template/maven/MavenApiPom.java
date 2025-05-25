package org.metalib.wiser.api.template.maven;

import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.template.maven.model.MavenModule;

import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;

public class MavenApiPom implements ApiWiserTemplateService {

    public static final String TEMPLATE_ID = "api-buddy::maven-module-api-pom";

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return API;
    }

    @Override
    public String fileExtension() {
        return XML;
    }

    @Override
    public boolean isSupportingFile() {
        return true;
    }

    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId() + DASH + moduleName();
            }

            @Override
            public String fileName() {
                return POM;
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        return MavenModule.API.enrich(bundle).toXml();
    }
}
