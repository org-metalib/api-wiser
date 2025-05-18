package org.metalib.wiser.api.template.maven.model;

import io.fabric8.maven.Maven;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3WriterEx;
import org.metalib.wiser.api.template.MavenDependency;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.metalib.wiser.api.template.ApiWiserFinals.POM_MODEL_ENCODING;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM_MODEL_VERSION;

@Data()
@Accessors(fluent = true, chain = true)
@Builder(toBuilder = true)
public class PomModel {

    File targetFile;

    String groupId;
    String artifactId;
    String version;

    String parentGroupId;
    String parentArtifactId;
    String parentVersion;

    String packaging;

    final Properties properties = new Properties();
    final List<Dependency> dependencyList = new ArrayList<>();
    final List<Dependency> dependencyManagementList = new ArrayList<>();
    final List<String> moduleList = new ArrayList<>();

    public PomModel modules(String... modules) {
        packaging("pom");
        for (final var module : modules) {
            moduleList.add(module);
            final var dependency = new Dependency();
            dependency.setGroupId(groupId);
            dependency.setArtifactId(module);
            dependency.setVersion(version);
            dependencyManagementList.add(dependency);
        }
        return this;
    }

    public PomModel properties(Properties p) {
        properties.putAll(p);
        return this;
    }

    public PomModel property(String name, String value) {
        properties.put(name, value);
        return this;
    }

    public PomModel dependencies(Dependency... dependencies) {
        dependencyList.addAll(Arrays.asList(dependencies));
        return this;
    }

    public PomModel dependency(String groupId, String artifactId) {
        return dependency(groupId, artifactId, null);
    }

    public PomModel dependency(MavenDependency coordinate) {
        return dependency(coordinate.getGroupId(), coordinate.getArtifactId(), null);
    }

    public PomModel dependency(MavenDependency coordinate, MavenScope scope) {
        return dependency(coordinate.getGroupId(), coordinate.getArtifactId(), scope);
    }

    public PomModel dependency(String groupId, String artifactId, MavenScope scope) {
        final var dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        if (null != scope) {
            dependency.setScope(scope.toString());
        }
        dependencyList.add(dependency);
        return this;
    }

    public PomModel dependenciesManagements(Dependency... dependencies) {
        dependencyManagementList.addAll(Arrays.asList(dependencies));
        return this;
    }

    public Model build() {
        return build(Maven.newModel());
    }

    public Model build(final Model result) {
        result.setModelEncoding(POM_MODEL_ENCODING);
        result.setModelVersion(POM_MODEL_VERSION);
        if (null != parentArtifactId && null != parentGroupId && null != parentVersion) {
            final var parent = new Parent();
            parent.setGroupId(parentGroupId);
            parent.setArtifactId(parentArtifactId);
            parent.setVersion(parentVersion);
            parent.setRelativePath("../pom.xml");
            result.setParent(parent);
        }
        if (!moduleList.isEmpty()) {
            result.setModules(moduleList);
        }
        if (null != packaging) {
            result.setPackaging(packaging);
        }
        if (null != groupId) {
            result.setGroupId(groupId);
        }
        if (null != artifactId) {
            result.setArtifactId(artifactId);
        }
        if (null != version) {
            result.setVersion(version);
        }
        if (!properties.isEmpty()) {
            result.setProperties(properties);
        }
        if (!dependencyList.isEmpty()) {
            result.setDependencies(dependencyList);
        }
        if (!dependencyManagementList.isEmpty()) {
            final var dependencyManagement = new DependencyManagement();
            dependencyManagement.setDependencies(dependencyManagementList);
            result.setDependencyManagement(dependencyManagement);
        }
        return result;
    }

    @SneakyThrows
    public String toXml() {
        final var output = new StringWriter();
        final var writer = new MavenXpp3WriterEx();
        writer.write(output, build());
        return toXml(null == targetFile? build() : build(Maven.readModel(targetFile.toPath())));
    }

    @SneakyThrows
    public static String toXml(Model model) {
        final var output = new StringWriter();
        Maven.writeModel(model, output);
        return output.toString();
    }
}
