package org.metalib.wiser.api.template.maven;

import io.fabric8.maven.Maven;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserMavenDependency;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplates;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.template.maven.model.PomModel;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.metalib.wiser.api.template.ApiWiserFinals.BLANK;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.X_API_WISER_MAVEN_BUILD_ROOT_PARENT;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.X_API_WISER_MAVEN_BUILD_ROOT;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.X_API_WISER_PROJECT_DIR;
import static org.metalib.wiser.api.template.maven.model.MavenModule.orderByDependency;
import static org.metalib.wiser.api.template.maven.model.PomModel.toXml;

public class MavenRootPom implements ApiWiserTemplateService {

    public static final String TEMPLATE_ID = "api-buddy::maven-project-pom";

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return ROOT;
    }

    @Override
    public String fileExtension() {
        return XML;
    }

    @Override
    public boolean isSupportingFile() {
        return true;
    }

    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return BLANK;
            }

            @Override
            public String fileName() {
                return POM;
            }
        };
    }

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var bundleExt = bundle.extraProperties();
        if (bundleExt == null) {
            return null;
        }
        final var model = Maven.readModel(bundle.targetFile().toPath());
        if (model == null) {
            return null;
        }

        final var clone = model.clone();
        clone.setParent(Optional.of(model).map(Model::getParent)
                .orElse((Parent)bundleExt.get(X_API_WISER_MAVEN_BUILD_ROOT_PARENT)));
        if (!POM.equals(model.getPackaging())) {
            clone.setPackaging(POM);
        }

        final var apiWiserCoord = (ApiWiserMavenDependency) bundleExt.get(X_API_WISER_MAVEN_BUILD_ROOT);
        final var rootProperties = new Properties();
        final var cloneProperties = clone.getProperties();
        rootProperties.setProperty("api-wiser.version", apiWiserCoord.getVersion());
        rootProperties.forEach(cloneProperties::putIfAbsent);

        // Modules
        final var bundleModules = bundle.modules();
        final var templateModules = new HashMap<String, Set<String>>();
        ApiWiserTemplates.list()
                .stream()
                // we are filtering out modules that do not produce any artifacts
                // it may cause some frustration when a template provider forgot to turn on any of templates
                // flags: isModelFile, isApiFile, or isSupportingFile
                .filter(v -> ApiWiserTemplates.anyModuleOutput(v.moduleName()))
                .forEach(v -> templateModules
                        .computeIfAbsent(v.moduleName(), k -> new TreeSet<>()).addAll(asList(v.moduleDependencies())));
        final var modules = orderByDependency(templateModules)
                .stream()
                .filter(m -> !m.equals(ROOT))
                .filter(m -> bundleModules.isEmpty() || bundleModules.contains(m))
                .map(v -> bundle.artifactId() + DASH + v)
                .collect(toList());
        final var moduleSet = new HashSet<>(modules);
        final var moduleList = clone.getModules().stream().filter(v -> !moduleSet.contains(v)).collect(toList());
        moduleList.addAll(modules);
        clone.setModules(moduleList);

        // Update DependencyManagement section
        final var dependencies = new Dependencies(clone);
        moduleList.forEach(v -> dependencies.findOrAdd(clone.getGroupId(), v).setVersion(clone.getVersion()));

        // Enforce pom.xml existence for each module
        final var projectDir = new File(bundleExt.get(X_API_WISER_PROJECT_DIR).toString());
        moduleList.forEach(moduleName -> {
            final var moduleProperties = new Properties();
            moduleProperties.setProperty("api-wiser.module", moduleName.substring(clone.getArtifactId().length()+1));

            final var parentRoot = new Parent();
            parentRoot.setGroupId(clone.getGroupId());
            parentRoot.setArtifactId(clone.getArtifactId());
            parentRoot.setVersion(clone.getVersion());

            final var moduleDir = new File(projectDir, moduleName);
            final var modulePomFile = new File(moduleDir, "pom.xml");
            final var modulePom = modulePomFile.exists() ? Maven.readModel(modulePomFile.toPath()) : Maven.newModel();

            // Enforce module parent to be the root module
            modulePom.setParent(parentRoot);
            modulePom.setGroupId(null);
            modulePom.setArtifactId(moduleName);
            modulePom.setVersion(null);

            // Enforce module properties with api wise enforceable properties
            modulePom.setProperties(moduleProperties);

            moduleDir.mkdirs();
            Maven.writeModel(modulePom, modulePomFile.toPath());
        });

        return toXml(clone);
    }
}

class Dependencies {

    final Model model;
    final DependencyManagement dependencyManagement;
    Dependencies(Model model) {
        this.model = model;
        this.dependencyManagement = Optional.of(model).map(ModelBase::getDependencyManagement).orElse(new DependencyManagement());
        model.setDependencyManagement(this.dependencyManagement);
    }

    Dependency findOrAdd(String groupId, String artifactId) {
        final var result = dependencyManagement.getDependencies().stream()
                .filter(v -> groupId.equals(v.getGroupId()) && artifactId.equals(v.getArtifactId()))
                .findFirst()
                .orElse(null);
        if (null == result) {
            final var d = new Dependency();
            d.setGroupId(groupId);
            d.setArtifactId(artifactId);
            dependencyManagement.addDependency(d);
            return d;
        } else {
            return result;
        }
    }
}
