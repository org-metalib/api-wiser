package org.metalib.wiser.api.mvn.plugin.conversion;

import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.API_WISER_MODULE_PROPERTY_NAME;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.BIZ;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAR;
import static org.metalib.wiser.api.template.ApiWiserFinals.MODEL;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM_MODEL_VERSION;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.SLASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;

/**
 * Converts Single Module Maven Project to Multi Module one.
 */
@RequiredArgsConstructor
public class SingleToMulti {

    static final String ORG_METALIB_API_WISER = "org.metalib.api.wiser";
    static final String API_WISER_MAVEN_TEMPLATES = "api-wiser-maven-templates";
    static final String ADDING_MODULE_TPL = "Adding module %s to project %s";
    static final String CREATED_MODULE_DIR_TPL = "Created module dir %s for project %s";
    static final String CREATED_MODULE_POM_TPL = "Created %s file for project %s";
    static final String[] MODULES = new String[]{MODEL, API, BIZ};

    final PluginDescriptor pluginDescriptor;
    final DefaultModelWriter modelWriter;
    final Log log;

    /**
     * Identifies if the project is defined as a single module project
     * @param project Project model
     * @return true if project model defined as a single module project
     */
    public static boolean isSinglePomProject(MavenProject project) {
        if (POM.equals(project.getPackaging())) {
            // https://www.baeldung.com/maven-packaging-types#4-pom
            // This project is an aggregator or parent pom project
            return false;
        }
        final var parent = project.getParent();
        if (parent == null) {
            return true;
        }
        return !project.getBasedir().getParentFile().equals(parent.getBasedir());
    }

    public boolean update(Model model) throws MojoExecutionException {
        final var dir = new File(model.getPomFile().getParent());
        final var packaging = model.getPackaging();
        if (POM.equals(packaging)) {
            return false;
        }
        log.info("Converting pom.xml to multi module project");
        model.setPackaging(POM);

        final var modelProperties = model.getProperties();
        if (!modelProperties.contains(API_WISER_MODULE_PROPERTY_NAME)) {
            modelProperties.setProperty(API_WISER_MODULE_PROPERTY_NAME, ROOT);
        }

        final var artifactId = model.getArtifactId();
        final var modules = model.getModules();
        final var moduleSet = new HashSet<>(model.getModules());

        for (var module : MODULES) {
            final var moduleArtifactId = artifactId + DASH + module;
            if (!moduleSet.contains(moduleArtifactId)) {
                log.info(format(ADDING_MODULE_TPL, moduleArtifactId, artifactId));
                modules.add(moduleArtifactId);
            }
            final var moduleDir = new File(dir, moduleArtifactId);
            if (moduleDir.mkdir()) {
                log.info(format(CREATED_MODULE_DIR_TPL, moduleArtifactId, artifactId));
            }
            final var pomFileName = POM + DOT + XML;
            final var modulePomFile = new File(moduleDir, pomFileName);
            if (!modulePomFile.canRead()) {
                final var moduleModel = createModuleModel(model, modulePomFile, moduleArtifactId);
                final var moduleProperties = moduleModel.getProperties();
                if (!moduleProperties.contains(API_WISER_MODULE_PROPERTY_NAME)) {
                    moduleProperties.setProperty(API_WISER_MODULE_PROPERTY_NAME, module);
                }
                try {
                    modelWriter.write(modulePomFile, Map.of(), moduleModel);
                    log.info(format(CREATED_MODULE_POM_TPL, moduleArtifactId + SLASH + pomFileName, artifactId));
                } catch (IOException e) {
                    throw new MojoExecutionException(format("Error creating %s file.", modulePomFile), e);
                }
            }
        }

        final var build = retrieveBuild(model);
        final var pluginManagement = retrievePluginManagement(build);
        final var plugins = pluginManagement.getPlugins();
        final var plugin = retrievePlugin(plugins, pluginDescriptor.getGroupId(), pluginDescriptor.getArtifactId(), pluginDescriptor.getVersion());
        final var pluginDependencies = plugin.getDependencies();
        if (checkAndSet(pluginDependencies, ORG_METALIB_API_WISER, API_WISER_MAVEN_TEMPLATES, pluginDescriptor.getVersion())) {
            log.info(format("Added %s:%s:%s dependency to %s:%s plugin.",
                    ORG_METALIB_API_WISER, API_WISER_MAVEN_TEMPLATES, pluginDescriptor.getVersion(),
                    pluginDescriptor.getGroupId(), pluginDescriptor.getArtifactId()));
        }
        return true;
    }

    static boolean checkAndSet(List<Dependency> dependencies, String groupId, String artifactId, String version) {
        for (var dependency : dependencies) {
            if (groupId.equals(dependency.getGroupId()) && artifactId.equals(dependency.getArtifactId())) {
                return false;
            }
        }
        final var dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependencies.add(dependency);
        return true;
    }

    static Plugin retrievePlugin(List<Plugin> plugins, String groupId, String artifactId, String version) {
        for (var plugin : plugins) {
            if (groupId.equals(plugin.getGroupId()) && artifactId.equals(plugin.getArtifactId())) {
                return plugin;
            }
        }
        final var plugin = new Plugin();
        plugin.setGroupId(groupId);
        plugin.setArtifactId(artifactId);
        plugin.setVersion(version);
        plugins.add(plugin);
        return plugin;
    }

    static PluginManagement retrievePluginManagement(Build build) throws MojoExecutionException {
        return Optional.of(build).map(Build::getPluginManagement).orElseGet(() -> {
            final var result = new PluginManagement();
            build.setPluginManagement(result);
            return result;
        });
    }

    static Build retrieveBuild(Model model) throws MojoExecutionException {
        return Optional.of(model).map(Model::getBuild).orElseGet(() -> {
            final var result = new Build();
            model.setBuild(result);
            return result;
        });
    }

    static Model createModuleModel(Model model, File modulePomFile, String moduleArtifactId) {
        final var moduleParent = new Parent();
        moduleParent.setGroupId(model.getGroupId());
        moduleParent.setArtifactId(model.getArtifactId());
        moduleParent.setVersion(model.getVersion());
        moduleParent.setRelativePath("../");
        final var moduleModel = new Model();
        moduleModel.setParent(moduleParent);
        moduleModel.setModelVersion(POM_MODEL_VERSION);
        moduleModel.setPackaging(JAR);
        moduleModel.setPomFile(modulePomFile);
        moduleModel.setArtifactId(moduleArtifactId);
        moduleModel.setVersion(model.getVersion());
        return moduleModel;
    }
}
