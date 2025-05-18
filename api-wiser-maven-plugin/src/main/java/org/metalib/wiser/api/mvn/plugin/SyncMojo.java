package org.metalib.wiser.api.mvn.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.metalib.wiser.api.core.java.code.ApiWiserCode;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;
import org.metalib.wiser.api.mvn.plugin.conversion.SingleToMulti;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserEvent;
import org.metalib.wiser.api.template.ApiWiserTemplates;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static java.lang.String.format;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_BUILD_ROOT_PARENT;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_BUILD_ROOT;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_DEPENDENCIES_NAME;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULE;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_BUILD_DIR;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_PROJECT_DIR;
import static org.metalib.wiser.api.mvn.plugin.conversion.SingleToMulti.isSinglePomProject;
import static org.metalib.wiser.api.template.ApiWiserEvents.MAVEN_SYNC;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;

/**
 * Initializes current maven project
 */
@Mojo( name = "sync", defaultPhase = LifecyclePhase.INITIALIZE)
public class SyncMojo extends AbstractMojo {

    static final String X_API_WISER_MAVEN_MODEL = "x" + DASH + API_WISER + DASH + "maven-model";

    @Inject
    DefaultModelReader modelReader;

    @Inject
    DefaultModelWriter modelWriter;

    @Inject
    MavenProject mavenProject;

    @Parameter( defaultValue = "${project.basedir}", property = "api-wiser.project-dir", required = true )
    private File projectDir;

    @Parameter( defaultValue = "${project.build.directory}", property = "api-wiser.project-build-dir", required = true )
    private File projectBuildDir;

    @Parameter( property = "api-wiser.project-package", required = true )
    private String projectPackage;

    private ObjectMapper jacksonYaml = new ObjectMapper(new YAMLFactory());

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Initializing Api Wiser controls");
        try {
            final var model = modelReader.read(mavenProject.getModel().getPomFile(), Map.of());

            if (isSinglePomProject(mavenProject)) {
                final var pluginDescriptor = (PluginDescriptor) this.getPluginContext().get("pluginDescriptor");
                if (new SingleToMulti(pluginDescriptor, modelWriter, getLog()).update(model)) {
                    modelWriter.write(new File(projectDir, "pom.xml"), Map.of(), model);
                }
                getLog().info("Completing Api Wiser controls. Please, run maven api:sync again.");
                return;
            }
            if (!ROOT.equals(moduleName())) {
                mavenProject.addCompileSourceRoot(new File(projectBuildDir, "generated-sources").toString());
                mavenProject.addTestCompileSourceRoot(new File(projectBuildDir, "generated-test-sources").toString());
            }
            try {
                final var apiWiserBuildProp = new ApiWiserBuildProperties();
                final var generator = ApiWiserCode.builder()
                        .dry(false)
                        .packageName(projectPackage)
                        .apiPackageName(projectPackage + ".api")
                        .modelPackageName(projectPackage + ".model")
                        .outputDir(outputDir())
                        .inputSpec(apiSpec())
                        .mavenGroupId(groupId())
                        .mavenArtifactId(artifactId())
                        .mavenVersion(version())
                        .additionalProperty(X_API_WISER_MODULE, moduleName())
                        .additionalProperty(X_API_WISER_MAVEN_BUILD_ROOT_PARENT, apiWiserBuildProp.rootParent())
                        .additionalProperty(X_API_WISER_MAVEN_BUILD_ROOT, apiWiserBuildProp.root())
                        .additionalProperty(X_API_WISER_PROJECT_DIR, projectDir)
                        .additionalProperty(X_API_WISER_PROJECT_BUILD_DIR, projectBuildDir)
                        .additionalProperty(X_API_WISER_MAVEN_MODEL, model)
                        .additionalProperty(X_API_WISER_MAVEN_DEPENDENCIES_NAME, List.of())
                        .build().generator();
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
                generator.generate();
            } catch (Exception e) {
                throw new MojoExecutionException("ApiWise Codegen Exception", e);
            }
/*
            final var model1 = modelReader.read(new File("pom.xml"), Map.of());
            final var modelProperties = model1.getProperties();
            model0.getProperties().forEach((k,v) -> {
                final var key = k.toString();
                final var value = v.toString();
                final var property = model1.getProperties().getProperty(key);
                if (null == property) {
                    modelProperties.setProperty(key, value);
                }
            });
            getLog().info("PluginArtifacts:");
            final var model1Build = Optional.of(model1)
                    .map(Model::getBuild)
                    .orElseGet(() -> {
                        final var build = new Build();
                        model1.setBuild(build);
                        return build;
                    });
            final var model1Plugins = Optional.of(model1Build)
                    .map(PluginContainer::getPlugins)
                    .orElseGet(() -> {
                        final var plugins = new ArrayList<Plugin>();
                        model1Build.setPlugins(plugins);
                        return plugins;
                    });
            final var model1PluginKeys = model1Plugins.stream().map(SyncMojo::pluginKey).collect(toSet());
            model0.getBuild().getPlugins().stream()
                    .filter(v -> !model1PluginKeys.contains(pluginKey(v)))
                    .forEach(plugin -> {
                        getLog().info(format("    adding <%s> plugin%n", plugin));
                        model1Plugins.add(plugin);
                    });
            getLog().info("PluginManagementArtifacts:");
            final var model1PluginManagement = Optional.of(model1Build)
                    .map(PluginConfiguration::getPluginManagement)
                    .orElseGet(() -> {
                        final var pluginManagement = new PluginManagement();
                        model1Build.setPluginManagement(pluginManagement);
                        return pluginManagement;
                    });
            final var model1PluginManagementPlugins = Optional.of(model1Build)
                    .map(PluginConfiguration::getPluginManagement)
                    .map(PluginContainer::getPlugins)
                    .orElseGet(() -> {
                        final var plugins = new ArrayList<Plugin>();
                        model1PluginManagement.setPlugins(plugins);
                        return plugins;
                    });
            final var model1PluginManagementKeys = model1PluginManagement.getPluginsAsMap().keySet();
            Optional.of(model0)
                    .map(Model::getBuild)
                    .map(PluginConfiguration::getPluginManagement)
                    .map(PluginContainer::getPlugins)
                    .stream()
                    .flatMap(Collection::stream)
                    .filter(v -> !model1PluginManagementKeys.contains(pluginKey(v)))
                    .forEach(plugin -> {
                        getLog().info(format("    adding <%s> plugin%n", plugin));
                        model1PluginManagementPlugins.add(plugin);
                    });
            modelWriter.write(new File(projectDir, "pom.xml"), Map.of(), model1);
 */
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String groupId() throws MojoExecutionException {
        return POM.equals(mavenProject.getPackaging())
                ? mavenProject.getGroupId()
                : Optional.of(mavenProject)
                .map(MavenProject::getParent)
                .map(MavenProject::getGroupId)
                .orElseThrow(()
                        -> new MojoExecutionException(format("GroupId is not specified for <%s> parent.",
                        mavenProject.getArtifactId())));
    }

    String artifactId() throws MojoExecutionException {
        return POM.equals(mavenProject.getPackaging())
                ? mavenProject.getArtifactId()
                : Optional.of(mavenProject)
                .map(MavenProject::getParent)
                .map(MavenProject::getArtifactId)
                .orElseThrow(()
                        -> new MojoExecutionException(format("ArtifactId is not specified for <%s> parent.",
                        mavenProject.getArtifactId())));
    }

    String version() throws MojoExecutionException {
        return POM.equals(mavenProject.getPackaging())
                ? mavenProject.getVersion()
                : Optional.of(mavenProject)
                .map(MavenProject::getParent)
                .map(MavenProject::getVersion)
                .orElseThrow(()
                        -> new MojoExecutionException(format("Version is not specified for <%s> parent.",
                        mavenProject.getArtifactId())));
    }

    File outputDir() throws MojoExecutionException {
        return POM.equals(mavenProject.getPackaging())
                ? projectDir
                : Optional.of(mavenProject)
                          .map(MavenProject::getParent)
                          .map(MavenProject::getBasedir)
                          .orElseThrow(()
                                  -> new MojoExecutionException(format("Parent directory is not specified for <%s> parent.",
                                                                mavenProject.getArtifactId())));
    }

    File apiSpec() throws MojoExecutionException {
        final var apiSpecName = POM.equals(mavenProject.getPackaging())
                ? mavenProject.getArtifactId()
                : Optional.of(mavenProject)
                          .map(MavenProject::getParent)
                          .map(MavenProject::getArtifact)
                          .map(Artifact::getArtifactId).orElse(null);
        if (null == apiSpecName) {
            throw new MojoExecutionException(format("Open API spec cannot be determined for project <%s>.", mavenProject.getArtifactId()));
        }
        final var apiSpec = new File(apiFileDir(), apiSpecName + ".yaml");
        if (apiSpec.canRead()) {
            return apiSpec;
        }
        throw new MojoExecutionException(format("Open API spec <%s> not found.", apiSpec));
    }

    File apiFileDir() {
        switch (moduleName()) {
            case "":
            case ROOT:
                return new File(projectDir, API);
            default:
                return new File(projectDir.getParent(), API);
        }
    }

    String moduleName() {
        return Optional.of(mavenProject).map(MavenProject::getProperties).map(v -> v.getProperty("api-wiser.module")).orElse("");
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