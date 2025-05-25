package org.metalib.wiser.api.template.maven;

import org.apache.maven.model.Model;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.template.maven.model.MavenModule;

import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.MODEL;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.X_API_WISER_MAVEN_MODEL;

public class MavenModelPom implements ApiWiserTemplateService {

    public static final String TEMPLATE_ID = "api-buddy::maven-module-model-pom";

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return MODEL;
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
        final var bundleExt = bundle.extraProperties();
        if (bundleExt == null) {
            return null;
        }
        final var model = (Model) bundleExt.get(X_API_WISER_MAVEN_MODEL);
        if (model == null) {
            return null;
        }
        final var moduleDependencies = bundle.moduleDependencies();
        return MavenModule.MODEL.enrich(bundle).toXml();
    }
}
