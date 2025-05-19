package org.metalib.wiser.api.template.jackson.http.client;

import org.apache.maven.model.Dependency;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserFinals;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.template.maven.model.MavenScope;
import org.metalib.wiser.api.template.maven.model.PomModel;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.MODULE_NAME;

/**
 * Template service for generating a Maven POM file for the HTTP client module.
 * 
 * <p>This template generates a Maven POM file that configures the HTTP client module
 * with the necessary dependencies and parent project relationship. It sets up dependencies
 * on the API module and other required libraries.</p>
 */
public class MavenHttpClientTemplate implements ApiWiserTemplateService {

    /** The name of this template */
    public static final String TEMPLATE_NAME = "maven-http-client";

    /** The unique identifier for this template */
    public static final String TEMPLATE_ID = API_WISER + DOUBLE_COLON + TEMPLATE_NAME;

    /**
     * Returns the unique identifier for this template.
     * 
     * @return the template ID
     */
    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    /**
     * Returns the name of the module this template belongs to.
     * 
     * @return the module name
     */
    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    /**
     * Returns the module dependencies required by this template.
     * 
     * @return an array of module names that this template depends on
     */
    @Override
    public String[] moduleDependencies() {
        return new String[]{API};
    }

    /**
     * Indicates that this template generates a supporting file rather than an API file.
     * 
     * @return true as this is a supporting file
     */
    @Override
    public boolean isSupportingFile() {
        return true;
    }

    /**
     * Returns the file extension for the generated file.
     * 
     * @return "xml" as the file extension
     */
    @Override
    public String fileExtension() {
        return XML;
    }

    /**
     * Defines the target file location for the generated Maven POM file.
     * 
     * @param config the API Wiser configuration
     * @return the target file information
     */
    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId() + DASH + MODULE_NAME;
            }

            @Override
            public String fileName() {
                return POM;
            }
        };
    }

    /**
     * Generates the XML content for the Maven POM file.
     * 
     * <p>This method creates a Maven POM file with the necessary configuration for the HTTP client module.
     * It sets up the parent project relationship, module properties, and dependencies.</p>
     * 
     * @param bundle the API Wiser bundle containing configuration and context
     * @return the generated XML content as a string
     */
    @Override
    public String toText(ApiWiserBundle bundle) {
        return PomModel.builder()
                .parentGroupId(bundle.groupId())
                .parentArtifactId(bundle.artifactId())
                .parentVersion(bundle.artifactVersion())
                .artifactId(bundle.artifactId() + DASH + MODULE_NAME)
                .build()
                .property(ApiWiserFinals.moduleNameProperty(), moduleName())
                .dependencies(stream(moduleDependencies())
                        .map(v -> toDependency(bundle, v))
                        .collect(toList()).toArray(Dependency[]::new))
                .dependency("org.metalib.net.url", "jersey-url-builder")
                .dependency("org.junit.jupiter", "junit-jupiter-engine", MavenScope.TEST)
                .toXml();
    }

    /**
     * Creates a Maven dependency for a module.
     * 
     * <p>This method creates a Maven dependency object for a module in the same project.</p>
     * 
     * @param bundle the API Wiser bundle containing configuration and context
     * @param moduleName the name of the module to create a dependency for
     * @return the Maven dependency object
     */
    static Dependency toDependency(ApiWiserBundle bundle, String moduleName) {
        final var result = new Dependency();
        result.setGroupId(bundle.groupId());
        result.setArtifactId(bundle.artifactId() + DASH + moduleName);
        return result;
    }
}
