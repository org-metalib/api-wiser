package org.metalib.wiser.api.template.maven.model;

import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class MvnPlugins {

    final List<Plugin> list;
    final Map<String, Plugin> map = new HashMap<>();
    boolean stripVersion = false;

    public static MvnPlugins create() {
        return new MvnPlugins(new ArrayList<>());
    }

    public static MvnPlugins wrap(List<Plugin> list) {
        final var result = new MvnPlugins(null == list? new ArrayList<>() : new ArrayList<>(list));
        Objects.requireNonNull(list).forEach(d -> result.map.put(key(d.getGroupId(), d.getArtifactId()), d));
        return result;
    }

    public MvnPlugins withVersionStripped() {
        stripVersion = true;
        return this;
    }

    public MvnPlugins withVersion() {
        stripVersion = false;
        return this;
    }


    public MvnPlugins add(Plugin plugin) {
        map.computeIfAbsent(
                key(plugin.getGroupId(), plugin.getArtifactId()),
                k -> {
                    final var d = copy(plugin);
                    if (stripVersion) {
                        d.setVersion(null);
                    }
                    list.add(d);
                    return d;
                }
        );
        return this;
    }

    public List<Plugin> list() {
        return list;
    }

    private static String key(String groupId, String artifactId) {
        return groupId + ":" + artifactId;
    }

    static Plugin copy(Plugin plugin) {
        final var result = new Plugin();
        result.setGroupId(plugin.getGroupId());
        result.setArtifactId(plugin.getArtifactId());
        result.setVersion(plugin.getVersion());
        result.setConfiguration(plugin.getConfiguration());
        result.setExecutions(new ArrayList<>(plugin.getExecutions().stream().map(MvnPlugins::copy).toList()));
        result.setDependencies(new ArrayList<>(plugin.getDependencies().stream().map(MvnDependencies::copy).toList()));
        return result;
    }

    static PluginExecution copy(PluginExecution source) {
        final var result = new PluginExecution();
        result.setId(source.getId());
        result.setPhase(source.getPhase());
        result.setGoals(new ArrayList<>(source.getGoals()));
        result.setPriority(source.getPriority());
        result.setConfiguration(source.getConfiguration());
        result.setInherited(source.getInherited());
        return result;
    }

    public MvnPlugin plugin() {
        return new MvnPlugin();
    }

    public class MvnPlugin {

        Plugin plugin = new Plugin();
        final Map<String, PluginExecution> executionMap = new HashMap<>();

        public MvnPlugin groupId(String groupId) {
            plugin.setGroupId(groupId);
            return this;
        }
        public MvnPlugin artifactId(String artifactId) {
            plugin.setArtifactId(artifactId);
            return this;
        }
        public MvnPlugin version(String version) {
            plugin.setVersion(version);
            return this;
        }

        public MvnPlugins add() {
            return MvnPlugins.this.add(plugin);
        }

        public MvnPluginExecution execution() {
            return new MvnPluginExecution();
        }

        MvnPlugin add(PluginExecution execution) {
            final var executions = Optional.ofNullable(plugin.getExecutions()).orElseGet(() -> {
                plugin.setExecutions(new ArrayList<>());
                return plugin.getExecutions();
            });
            final var old = executionMap.get(execution.getId());
            if (null != old) {
                executions.remove(old);
            }
            executions.add(execution);
            executionMap.put(execution.getId(), execution);
            return this;
        }

        public class MvnPluginExecution {

            PluginExecution execution = new PluginExecution();

            public MvnPluginExecution id(String value) {
                execution.setId(value);
                return this;
            }

            public MvnPluginExecution goals(String... values) {
                execution.setGoals(Arrays.asList(values));
                return this;
            }

            public MvnPluginExecution phase(String value) {
                execution.setPhase(value);
                return this;
            }

            public MvnPluginExecution priority(int value) {
                execution.setPriority(value);
                return this;
            }

            public MvnPluginExecution inherited(boolean value) {
                execution.setInherited(value);
                return this;
            }

            public MvnPluginExecution inherited(String value) {
                execution.setInherited(value);
                return this;
            }

            public MvnPlugin add() {
                if (null == execution.getId()) {
                    execution.setId("default");
                }
                return MvnPlugin.this.add(execution);
            }

            public MvnPluginExecution configuration(Object value) {
                execution.setConfiguration(value);
                return this;
            }
        }
    }
}
