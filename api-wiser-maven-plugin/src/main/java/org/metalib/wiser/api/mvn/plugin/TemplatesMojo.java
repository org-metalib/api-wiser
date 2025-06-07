package org.metalib.wiser.api.mvn.plugin;

import io.fabric8.maven.Maven;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginContainer;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.metalib.wiser.api.template.maven.model.MvnDependencies;
import org.metalib.wiser.api.template.maven.model.MvnPlugins;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM_DOT_XML;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;
import static org.metalib.wiser.api.template.maven.model.MvnDependencies.toDependency;
import static org.metalib.wiser.api.template.maven.model.PomModel.toXml;

@Mojo( name = TemplatesMojo.MOJO_NAME, defaultPhase = LifecyclePhase.INITIALIZE)
public class TemplatesMojo extends ApiWiserAbstractMojo {

    static final String MOJO_NAME = "templates";

    /**
     * A list of api-wiser template components.
     * <p>
     * This parameter specifies a collection of maven coordinates to be injected as dependencies for api-wiser-maven-plugin.
     * The value is required and must be provided via the property "api-wiser.templates".
     */
    @Parameter( name = "templates", property = "api-wiser.templates", required = true )
    private List<String> templates;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (!isRootModule()) {
            getLog().info(format("The %s mojo is applicable on to the %s module only.",  MOJO_NAME, ROOT));
            return;
        }

        final var pomXmlFile = new File(POM_DOT_XML);
        final var model = Maven.readModel(pomXmlFile.toPath());
        if (null == model) {
            throw new MojoFailureException(format("File %s not found.", POM_DOT_XML));
        }

        getLog().info( "Templates: " + templates );
        final var build = Optional.of(model).map(Model::getBuild).orElseGet(() -> {
            final var value = new Build();
            model.setBuild(value);
            return value;
        });

        final var pluginManagement = Optional.of(build).map(Build::getPluginManagement).orElseGet(() -> {
            final var value = new PluginManagement();
            build.setPluginManagement(value);
            return value;
        });

        final var mvnPlugins = MvnPlugins.wrap(Optional.of(pluginManagement).map(PluginContainer::getPlugins).orElseGet(ArrayList::new));
        final var plugin = mvnPlugins.findOrAdd("org.metalib.api.wiser", "api-wiser-maven-plugin");
        if (null == plugin.getVersion()) {
            plugin.setVersion("${api-wiser.version}");
        }

        final var executions = Optional.of(plugin).map(Plugin::getExecutions).orElseGet(() -> {
            final var value = new ArrayList<PluginExecution>();
            plugin.setExecutions(value);
            return value;
        });

        final var execution = executions.stream().filter(v -> "default".equals(v.getId())).findFirst().orElseGet(() -> {
            final var result = new PluginExecution();
            executions.add(result);
            return result;
        });
        execution.setPhase("initialize");
        execution.setGoals(new ArrayList<>(List.of("sync")));

        final var dependencies = MvnDependencies
                .wrap(Optional.of(plugin)
                        .map(Plugin::getDependencies)
                        .orElseGet(ArrayList::new));
        templates.forEach(template -> dependencies.add(toDependency(template)));
        plugin.setDependencies(dependencies.list());

        try {
            Files.write(pomXmlFile.toPath(), toXml(model).getBytes());
        } catch (IOException e) {
            throw new MojoExecutionException(e);
        }
    }
}
