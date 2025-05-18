package org.metalib.wiser.api.template.model.gson;

import com.squareup.javapoet.JavaFile;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.model.jackson.ModelBuilder.createModelBuilder;

public class GsonModelTemplate implements ApiWiserTemplateService {
    public static final String TEMPLATE_NAME = "model-gson";
    public static final String TEMPLATE_ID = "api-wiser::" + TEMPLATE_NAME;

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
        return false; // replace to `true` when the implementation is ready.
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
                        + File.separator + TEMPLATE_NAME.replace(DASH, DOT);
            }

            @Override
            public String fileName() {
                return config.toModelFilename(config.baseEntityName());
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var modelPackage = bundle.basePackage() + DOT + TEMPLATE_NAME.replace(DASH, DOT);
        final var model = createModelBuilder(bundle).build();
        return JavaFile.builder(modelPackage, model).build().toString();
    }
}
