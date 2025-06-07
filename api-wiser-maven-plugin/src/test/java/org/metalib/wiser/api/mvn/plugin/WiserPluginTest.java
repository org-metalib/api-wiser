package org.metalib.wiser.api.mvn.plugin;

import io.fabric8.maven.Maven;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginContainer;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.PluginManagement;
import org.junit.Test;
import org.metalib.wiser.api.template.maven.model.MvnDependencies;
import org.metalib.wiser.api.template.maven.model.MvnPlugins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM_DOT_XML;
import static org.metalib.wiser.api.template.maven.model.MvnDependencies.toDependency;

public class WiserPluginTest {

    @Test
    public void test() {
        final var pomXmlFile = new File(POM_DOT_XML);
        final var model = Maven.readModel(pomXmlFile.toPath());

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

        final var plugins = MvnPlugins.wrap(Optional.of(pluginManagement).map(PluginContainer::getPlugins).orElseGet(ArrayList::new));
        final var plugin = plugins.findOrAdd("org.metalib.api.wiser", "api-wiser-maven-plugin");
        if (null == plugin.getVersion()) {
            plugin.setVersion("${api-wiser.version}");
        }

        final var executions = Optional.of(plugin).map(Plugin::getExecutions).orElseGet(() -> {
            final var value = new ArrayList<PluginExecution>();
            plugin.setExecutions(value);
            return value;
        });

        executions.stream().filter(v -> null == v.getId() || !"default".equals(v.getId())).findFirst().orElseGet(() -> {
            final var execution = new PluginExecution();
            execution.setPhase("initialize");
            execution.setGoals(new ArrayList<>(List.of("sync")));
            executions.add(execution);
            return execution;
        });

        final var dependencies = MvnDependencies.wrap(Optional.of(plugin).map(Plugin::getDependencies).orElseGet(ArrayList::new))
                .add(toDependency("org.metalib.api.wiser:api-wiser-maven-templates:${api-wiser.version}"))
                .add(toDependency("org.metalib.api.wiser:api-wiser-spring-app-templates:${api-wiser.version}"))
        ;
        pluginManagement.setPlugins(plugins.list());

        assertEquals(2, dependencies.list().size());

        return;
    }
}
