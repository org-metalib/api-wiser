package org.metalib.wiser.api.template.spring;

import com.palantir.javapoet.JavaFile;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.ApiWiserFinals.MD;
import static org.metalib.wiser.api.template.ApiWiserFinals.README;
import static org.metalib.wiser.api.template.spring.SpringAppMavenTemplate.TEMPLATE_NAME;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.DASH;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.MODULE_NAME;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.createSpringAppClassBuilder;

public class SpringAppReadmeTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_ID = "api-wiser::" + TEMPLATE_NAME + DASH + README;

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    @Override
    public String fileExtension() {
        return MD;
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
                return config.mavenArtifactId() + DASH + MODULE_NAME;
            }

            @Override
            public String fileName() {
                return README;
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        return bundle.targetFile().exists()
                ? null // // do not overwrite existing file
                : """
                  # Spring Boot App
                  
                  Start Spring Boot Application locally
                  ```shell
                  mvn spring-boot:run
                  ```
                  """;
    }
}
