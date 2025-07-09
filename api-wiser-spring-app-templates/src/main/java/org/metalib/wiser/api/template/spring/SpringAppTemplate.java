package org.metalib.wiser.api.template.spring;

import com.palantir.javapoet.JavaFile;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.createSpringAppClassBuilder;

public class SpringAppTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "spring-app";
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
    public boolean isSupportingFile() {
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
                        + File.separator
                        + config.generatedSourceFolder()
                        + File.separator
                        + config.basePackage().replace(".", File.separator);
            }

            @Override
            public String fileName() {
                return "SpringApp";
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var springAppPackage = bundle.basePackage();
        return JavaFile.builder(springAppPackage, createSpringAppClassBuilder(bundle).build())
                .skipJavaLangImports(true)
                .build().toString();
    }
}
