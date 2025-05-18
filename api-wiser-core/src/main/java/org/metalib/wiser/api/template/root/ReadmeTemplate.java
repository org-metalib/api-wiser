package org.metalib.wiser.api.template.root;

import lombok.SneakyThrows;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.MD;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;

public class ReadmeTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "README";
    public static final String TEMPLATE_ID = "api-wiser::" + TEMPLATE_NAME;

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return ROOT;
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
                return "";
            }

            @Override
            public String fileName() {
                return TEMPLATE_NAME;
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var outputFile = new File(bundle.projectDir(), TEMPLATE_NAME + DOT + fileExtension());
        return outputFile.isFile()
                ? readFileAsString(outputFile.getAbsolutePath())
                : ReadMeBuilder.createProjectReadMe(bundle);
    }

    @SneakyThrows
    static String readFileAsString(String fileName) {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
