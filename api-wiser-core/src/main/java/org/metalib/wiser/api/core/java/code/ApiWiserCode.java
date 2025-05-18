package org.metalib.wiser.api.core.java.code;

import static lombok.AccessLevel.PRIVATE;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULES;
import static org.metalib.wiser.api.template.ApiWiserFinals.X_API_WISER_TARGET_FILE;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import org.metalib.wiser.api.template.ApiWiserTemplates;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.Generator;
import org.openapitools.codegen.TemplateManager;
import org.openapitools.codegen.api.TemplatePathLocator;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.openapitools.codegen.templating.CommonTemplateContentLocator;
import org.openapitools.codegen.templating.GeneratorTemplateContentLocator;
import org.openapitools.codegen.templating.TemplateManagerOptions;

@AllArgsConstructor(access = PRIVATE)
@Builder
public class ApiWiserCode {

    boolean dry;
    String packageName;
    String apiPackageName;
    String clientPackageName;
    String serverPackageName;
    String modelPackageName;
    File outputDir;
    File inputSpec;
    String mavenGroupId;
    String mavenArtifactId;
    String mavenVersion;

    @Singular
    final Map<String, Object> additionalProperties;

    public ApiWiserGenerator generator() {
        return new ApiWiserGenerator(
                dry,
                packageName,
                apiPackageName,
                clientPackageName,
                serverPackageName,
                modelPackageName,
                outputDir,
                inputSpec,
                mavenGroupId,
                mavenArtifactId,
                mavenVersion,
                additionalProperties);
    }
}
