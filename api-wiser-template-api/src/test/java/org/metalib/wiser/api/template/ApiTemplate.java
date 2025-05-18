package org.metalib.wiser.api.template;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;

public class ApiTemplate implements ApiWiserTemplateService {
    public static final String TEMPLATE_NAME = API;
    public static final String TEMPLATE_ID = "api-wiser::" + TEMPLATE_NAME;
    public static final String DOT = ".";

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return TEMPLATE_NAME;
    }

    @Override
    public String[] moduleDependencies() {
        return new String[] {API};
    }

    @Override
    public String fileExtension() {
        return JAVA;
    }

    @Override
    public boolean isApiFile() {
        return true;
    }

    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId()
                        + "-"
                        + TEMPLATE_NAME
                        + File.separator + config.sourceFolder()
                        + File.separator + config.basePackage().replace(DOT, File.separator)
                        + File.separator + TEMPLATE_NAME;
            }

            @Override
            public String fileName() {
                return config.camelizeBaseEntityName() + "Api";
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var bizPackage = bundle.basePackage() + DOT + TEMPLATE_NAME;
        return "";
    }
}
