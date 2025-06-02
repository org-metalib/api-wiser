package org.metalib.wiser.api.template.spring.webclient;

import io.fabric8.maven.Maven;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Parent;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.Xpp3DomUtils;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserFinals;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.template.maven.model.MvnDependencies;
import org.metalib.wiser.api.template.maven.model.MvnPlugins;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import static java.util.Arrays.stream;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.XML;
import static org.metalib.wiser.api.template.maven.model.PomModel.toXml;
import static org.metalib.wiser.api.template.spring.webclient.SpringWebClientTemplateBuilder.MODULE_NAME;

/**
 * Template service for generating a Maven POM file for the HTTP client module.
 * 
 * <p>This template generates a Maven POM file that configures the HTTP client module
 * with the necessary dependencies and parent project relationship. It sets up dependencies
 * on the API module and other required libraries.</p>
 */
public class SpringWebClientMavenTemplate implements ApiWiserTemplateService {

    /** The name of this template */
    public static final String TEMPLATE_NAME = "spring-webclient-maven";

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
    @SneakyThrows
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
        final var cloneOpt = Optional.of(clone);
        final var parent = cloneOpt.map(Model::getParent).orElseGet(Parent::new);
        parent.setGroupId(bundle.groupId());
        parent.setArtifactId(bundle.artifactId());
        parent.setVersion(bundle.artifactVersion());
        clone.setArtifactId(bundle.artifactId() + DASH + MODULE_NAME);

        final var properties = cloneOpt.map(ModelBase::getProperties).orElseGet(Properties::new);
        properties.setProperty(ApiWiserFinals.moduleNameProperty(), moduleName());

        final var dependencies = MvnDependencies.wrap(stream(moduleDependencies())
                        .map(v -> toDependency(bundle, v))
                        .toList())
                .withVersionStripped()
                .add("org.springframework.boot", "spring-boot-starter-webflux");
        Optional.of(clone).map(ModelBase::getDependencies).stream().flatMap(Collection::stream).forEach(dependencies::add);
        clone.setDependencies(dependencies.list());

//        final var build = Optional.of(clone).map(Model::getBuild).orElseGet(() -> {
//            final var result = new Build();
//            clone.setBuild(result);
//            return result;
//        });
//        final var sources = new Xpp3Dom("sources");
//        final var source = new Xpp3Dom("source");
//        source.setParent(sources);
//        source.setValue("${project.build.directory}/" + bundle.generatedSourceFolder());
//        sources.addChild(source);
//        final var plugins = MvnPlugins.wrap(build.getPlugins())
//                .withVersionStripped()
//                .plugin()
//                    .groupId("org.codehaus.mojo")
//                    .artifactId("build-helper-maven-plugin")
//                    .execution()
//                        .goals("add-source")
//                        .phase("generate-sources")
//                        .configuration(sources)
//                        .add()
//                    .add();
//        build.setPlugins(plugins.list());
        return toXml(clone);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Sources {
        List<Source> sources;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder(toBuilder = true)
        public static class Source {
            List<String> source;
        }
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
