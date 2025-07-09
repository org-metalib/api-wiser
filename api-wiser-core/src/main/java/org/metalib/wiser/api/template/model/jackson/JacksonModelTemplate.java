package org.metalib.wiser.api.template.model.jackson;

import com.palantir.javapoet.JavaFile;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.ApiWiserFinals.MODEL;
import static org.metalib.wiser.api.template.model.jackson.ModelBuilder.createModelBuilder;

public class JacksonModelTemplate implements ApiWiserTemplateService {
    public static final String TEMPLATE_NAME = MODEL;
    public static final String TEMPLATE_ID = API_WISER + DOUBLE_COLON + TEMPLATE_NAME;

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return TEMPLATE_NAME;
    }

    @Override
    public String fileExtension() {
        return JAVA;
    }

    @Override
    public boolean isModelFile() {
        return true;
    }

    @Override
    public String[] mavenDependencies() {
        return new String[]{
                "com.fasterxml.jackson.core:jackson-annotations",
        };
    }

    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId()
                        + "-"
                        + TEMPLATE_NAME
                        + File.separator + config.generatedSourceFolder()
                        + File.separator + config.basePackage().replace(DOT, File.separator)
                        + File.separator + TEMPLATE_NAME;
            }

            @Override
            public String fileName() {
                return config.toModelFilename(config.baseEntityName());
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var modelPackage = bundle.basePackage() + DOT + TEMPLATE_NAME;
        final var model = createModelBuilder(bundle).build();
        return JavaFile.builder(modelPackage, model).skipJavaLangImports(true).build().toString();
    }

}
