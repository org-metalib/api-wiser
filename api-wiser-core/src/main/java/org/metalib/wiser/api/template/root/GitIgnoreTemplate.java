package org.metalib.wiser.api.template.root;

import lombok.SneakyThrows;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_DIR;
import static org.metalib.wiser.api.template.ApiWiserFinals.EMPTY;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;

public class GitIgnoreTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "git-ignore";
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
        return "gitignore";
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
                return EMPTY;
            }

            @Override
            public String fileName() {
                return EMPTY;
            }
        };
    }

    @Override
    @SneakyThrows
    public String toText(ApiWiserBundle bundle) {
        final var lines = bundle.targetFile().exists()
                ? Files.readAllLines(bundle.targetFile().toPath(), UTF_8)
                : List.<String>of();
        final var uniqueLines = new LinkedHashSet<>(lines); // Remove duplicates by adding them to a Set
        final var result = GitIgnoreBuilder.createGitIgnore(bundle);
        final var filteredResult = Arrays.stream(result.split("\\R"))
                .filter(line -> !uniqueLines.contains(line))
                .toList();
        return (lines.isEmpty() ? "" : String.join(System.lineSeparator(), lines) + System.lineSeparator())
                + String.join(System.lineSeparator(), filteredResult);
    }
}
