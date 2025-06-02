package org.metalib.wiser.api.template.biz;

import com.palantir.javapoet.JavaFile;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;

import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.biz.BizBuilder.createBizBuilder;
import static org.springframework.util.StringUtils.capitalize;

public class BizTemplate implements ApiWiserTemplateService {
    public static final String TEMPLATE_NAME = "biz";
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
                return config.camelizeBaseEntityName() + capitalize(TEMPLATE_NAME);
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        if (bundle.targetFile().exists()) {
            return null;
        }
        final var bizPackage = bundle.basePackage() + DOT + TEMPLATE_NAME;
        final var bizServer = createBizBuilder(bundle).build();
        return JavaFile.builder(bizPackage, bizServer).skipJavaLangImports(true).build().toString();
    }
}
