package org.metalib.wiser.api.template.spring;

import com.palantir.javapoet.JavaFile;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.BIZ;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.CONTROLLER;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.DOT;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.createSpringControllerBuilder;
import static org.springframework.util.StringUtils.capitalize;

public class SpringControllerTemplate implements ApiWiserTemplateService {
    public static final String TEMPLATE_NAME = "spring-" + CONTROLLER;
    public static final String TEMPLATE_ID = "api-wiser::" + TEMPLATE_NAME;

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return "spring-app";
    }

    @Override
    public String[] moduleDependencies() {
        return new String[]{BIZ};
    }

    @Override
    public boolean isApiFile() {
        return true;
    }

    @Override
    public String fileExtension() {
        return JAVA;
    }

    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId()
                        + "-"
                        + SpringAppTemplate.TEMPLATE_NAME
                        + File.separator + config.generatedSourceFolder()
                        + File.separator + config.basePackage().replace(DOT, File.separator)
                        + File.separator + CONTROLLER;
            }

            @Override
            public String fileName() {
                return config.camelizeBaseEntityName() + capitalize(CONTROLLER);
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var springServerPackage = bundle.basePackage() + DOT + CONTROLLER;
        final var springServer = createSpringControllerBuilder(bundle).build();
        return JavaFile.builder(springServerPackage, springServer).build().toString();
    }
}
