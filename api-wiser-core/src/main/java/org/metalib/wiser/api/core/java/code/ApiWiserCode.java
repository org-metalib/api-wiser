package org.metalib.wiser.api.core.java.code;

import static lombok.AccessLevel.PRIVATE;

import java.io.File;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;

/**
 * A builder class for creating and configuring {@link ApiWiserGenerator} instances.
 * This class provides a fluent interface for setting various configuration options
 * for code generation from OpenAPI specifications.
 */
@AllArgsConstructor(access = PRIVATE)
@Builder
public class ApiWiserCode {

    /**
     * Flag indicating whether to perform a dry run (no files will be written).
     */
    boolean dry;

    /**
     * The base package name for generated code.
     */
    String packageName;

    /**
     * The package name for generated API interfaces.
     */
    String apiPackageName;

    /**
     * The package name for generated client classes.
     */
    String clientPackageName;

    /**
     * The package name for generated server classes.
     */
    String serverPackageName;

    /**
     * The package name for generated model classes.
     */
    String modelPackageName;

    /**
     * The output directory where generated files will be written.
     */
    File outputDir;

    /**
     * The input OpenAPI specification file.
     */
    File inputSpec;

    /**
     * The Maven group ID for generated projects.
     */
    String mavenGroupId;

    /**
     * The Maven artifact ID for generated projects.
     */
    String mavenArtifactId;

    /**
     * The Maven version for generated projects.
     */
    String mavenVersion;

    /**
     * Additional properties to be passed to the generator.
     */
    @Singular
    final Map<String, Object> additionalProperties;

    /**
     * Creates and returns a new {@link ApiWiserGenerator} instance configured with
     * the properties set in this builder.
     *
     * @return A new ApiWiserGenerator instance
     */
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
