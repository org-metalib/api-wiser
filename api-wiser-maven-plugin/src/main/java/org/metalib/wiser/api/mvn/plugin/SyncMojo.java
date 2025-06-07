package org.metalib.wiser.api.mvn.plugin;

import lombok.SneakyThrows;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.metalib.wiser.api.core.java.code.ApiWiserCode;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;
import org.metalib.wiser.api.mvn.plugin.conversion.SingleToMulti;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserEvent;
import org.metalib.wiser.api.template.ApiWiserTemplates;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_BUILD_ROOT_PARENT;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_BUILD_ROOT;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_DEPENDENCIES_NAME;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULE;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_BUILD_DIR;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_DIR;
import static org.metalib.wiser.api.mvn.plugin.conversion.SingleToMulti.isSinglePomProject;
import static org.metalib.wiser.api.template.ApiWiserEvents.MAVEN_SYNC;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;

/**
 * Initializes API Wiser controls for a Maven project.
 * <p>
 * This goal can:
 * 1. Convert a single-module project to a multi-module project
 * 2. Generate code based on an API specification file
 * 3. Set up source directories for the generated code
 * 4. Configure Maven project properties and dependencies
 */
@Mojo( name = "sync", defaultPhase = LifecyclePhase.INITIALIZE)
public class SyncMojo extends ApiWiserAbstractMojo {

    static final String X_API_WISER_MAVEN_MODEL = "x" + DASH + API_WISER + DASH + "maven-model";

    /**
     * Maven model reader for reading POM files
     */
    @Inject
    DefaultModelReader modelReader;

    /**
     * Maven model writer for writing POM files
     */
    @Inject
    DefaultModelWriter modelWriter;

    /**
     * Executes the goal.
     * <p>
     * This method initializes API Wiser controls for the Maven project.
     * If the project is a single-module project, it will be converted to a multi-module project.
     * Otherwise, it will set up source directories and generate code based on the API specification.
     * 
     * @throws MojoExecutionException if an error occurs during execution
     * @throws MojoFailureException if the execution fails
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Initializing Api Wiser controls");
        try {
            // Read the project's POM file
            final var model = modelReader.read(mavenProject.getModel().getPomFile(), Map.of());

            // API Wiser module name
            final var wiserModule = moduleName();
            if (wiserModule.isBlank()) {
                getLog().warn("Api Wiser does not manage: " + mavenProject.getArtifactId() + " module.");
                return;
            }

            // If this is a single-module project, convert it to a multi-module project
            if (isSinglePomProject(mavenProject)) {
                final var pluginDescriptor = (PluginDescriptor) this.getPluginContext().get("pluginDescriptor");
                if (new SingleToMulti(pluginDescriptor, modelWriter, getLog()).update(model)) {
                    modelWriter.write(new File(projectDir, "pom.xml"), Map.of(), model);
                }
                getLog().info("Completing Api Wiser controls. Please, run maven api:sync again.");
                return;
            }

            // If this is not the root module, add source directories
            if (!ROOT.equals(wiserModule)) {
                mavenProject.addCompileSourceRoot(new File(projectBuildDir, "generated-sources").toString());
                mavenProject.addTestCompileSourceRoot(new File(projectBuildDir, "generated-test-sources").toString());
            }

            try {
                // Create a generator for the API code
                final var apiWiserBuildProp = new ApiWiserBuildProperties();
                final var generator = ApiWiserCode.builder()
                        .dry(false)
                        .packageName(projectPackage)
                        .apiPackageName(projectPackage + ".api")
                        .modelPackageName(projectPackage + ".model")
                        .outputDir(outputDir())
                        .inputSpec(apiSpec(wiserModule))
                        .mavenGroupId(groupId())
                        .mavenArtifactId(artifactId())
                        .mavenVersion(version())
                        .additionalProperty(X_API_WISER_MODULE, wiserModule)
                        .additionalProperty(X_API_WISER_MAVEN_BUILD_ROOT_PARENT, apiWiserBuildProp.rootParent())
                        .additionalProperty(X_API_WISER_MAVEN_BUILD_ROOT, apiWiserBuildProp.root())
                        .additionalProperty(X_API_WISER_PROJECT_DIR, projectDir)
                        .additionalProperty(X_API_WISER_PROJECT_BUILD_DIR, projectBuildDir)
                        .additionalProperty(X_API_WISER_MAVEN_MODEL, model)
                        .additionalProperty(X_API_WISER_MAVEN_DEPENDENCIES_NAME, List.of())
                        .build().generator();

                // Configure the generator
                final var config = generator.config();
                ApiWiserTemplates.list().forEach(v -> v.listener(new ApiWiserEvent() {
                    @Override
                    public String name() {
                        return MAVEN_SYNC;
                    }

                    @Override
                    public ApiWiserConfig config() {
                        return config;
                    }
                }));

                // Generate the code
                generator.generate();
            } catch (Exception e) {
                throw new MojoExecutionException("ApiWise Codegen Exception", e);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

class ApiWiserBuildProperties {

    Properties properties = loadApiWiserBuildProperties();

    Parent rootParent() {
        final var result = new Parent();
        result.setGroupId(properties.getProperty("api-wiser.root.parent.group-id"));
        result.setArtifactId(properties.getProperty("api-wiser.root.parent.artifact-id"));
        result.setVersion(properties.getProperty("api-wiser.root.parent.version"));
        return result;
    }

    ApiWiserMavenDependency root() {
        return ApiWiserMavenDependency.builder()
                .groupId(properties.getProperty("api-wiser.root.group-id"))
                .artifactId(properties.getProperty("api-wiser.root.artifact-id"))
                .version(properties.getProperty("api-wiser.root.version"))
                .build();
    }

    @SneakyThrows
    Properties loadApiWiserBuildProperties() {
        try (var inputStream = SyncMojo.class.getResourceAsStream("/api-wiser-build.properties")) {
            final var result = new Properties();
            result.load(inputStream);
            return result;
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to load properties file.", e);
        }
    }
}
