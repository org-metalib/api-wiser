package org.metalib.wiser.api.template.spring;

import io.fabric8.maven.Maven;
import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.Parent;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserFinals;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.template.maven.model.MvnDependencies;
import org.metalib.wiser.api.template.maven.model.MvnPlugins;

import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static java.util.Arrays.stream;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER;
import static org.metalib.wiser.api.template.ApiWiserFinals.BIZ;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOUBLE_COLON;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_STARTER_TEST;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_STARTER_WEB;
import static org.metalib.wiser.api.template.maven.model.PomModel.toXml;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.DASH;
import static org.metalib.wiser.api.template.spring.SpringTemplateBuilder.MODULE_NAME;

public class SpringAppMavenTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "spring-app-pom";
    public static final String TEMPLATE_ID = API_WISER + DOUBLE_COLON + TEMPLATE_NAME;
    static final String POM = "pom";
    static final String XML = "xml";

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    @Override
    public String[] moduleDependencies() {
        return new String[]{BIZ};
    }

    @Override
    public boolean isSupportingFile() {
        return true;
    }

    @Override
    public String fileExtension() {
        return XML;
    }

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
                .add(SPRING_BOOT_STARTER_WEB.toDependency())
                .addTest(SPRING_BOOT_STARTER_TEST.toDependency());
        Optional.of(clone).map(ModelBase::getDependencies).stream().flatMap(Collection::stream).forEach(dependencies::add);
        clone.setDependencies(dependencies.list());

        final var build = Optional.of(clone).map(Model::getBuild).orElseGet(() -> {
            final var result = new Build();
            clone.setBuild(result);
            return result;
        });
        final var plugins = MvnPlugins.wrap(build.getPlugins())
                .withVersionStripped()
                .plugin().groupId("org.springframework.boot").artifactId("spring-boot-maven-plugin").add();
        build.setPlugins(plugins.list());

        return toXml(clone);
    }

    static Dependency toDependency(ApiWiserBundle bundle, String moduleName) {
        final var result = new Dependency();
        result.setGroupId(bundle.groupId());
        result.setArtifactId(bundle.artifactId() + DASH + moduleName);
        return result;
    }
}
