package org.metalib.wiser.api.template.api;

import com.palantir.javapoet.JavaFile;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.api.ApiBuilder.createApiBuilder;
import static org.springframework.util.StringUtils.capitalize;

public class ApiTemplate implements ApiWiserTemplateService {
    public static final String TEMPLATE_NAME = "api";
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
                        + File.separator + config.generatedSourceFolder()
                        + File.separator + config.basePackage().replace(DOT, File.separator)
                        + File.separator + TEMPLATE_NAME;
            }
            @Override
            public String fileName() {
                return config.camelizeBaseEntityName() + capitalize(TEMPLATE_NAME);
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var apiPackage = bundle.basePackage() + DOT + TEMPLATE_NAME;
        final var api = createApiBuilder(bundle).build();
        return JavaFile.builder(apiPackage, api).skipJavaLangImports(true).build().toString();
    }

}
