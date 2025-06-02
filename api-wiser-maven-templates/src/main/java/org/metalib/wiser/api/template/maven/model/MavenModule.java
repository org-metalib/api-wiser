package org.metalib.wiser.api.template.maven.model;

import org.apache.maven.model.Dependency;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserFinals;
import org.metalib.wiser.api.template.ApiWiserTemplates;
import org.metalib.wiser.api.template.maven.MavenProjectHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.DOT_VERSION;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.IMPORT;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.MAVEN_DEPENDENCY_VERSIONS;
import static org.metalib.wiser.api.template.maven.MavenProjectHelper.toPomModelBuilder;
import static org.metalib.wiser.api.template.maven.model.MavenLib.JACKSON_ANNOTATION;
import static org.metalib.wiser.api.template.maven.model.MavenLib.LOMBOK;
import static org.metalib.wiser.api.template.maven.model.MavenLib.MAPSTRUCT;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_STARTER_TEST;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_STARTER_WEB;

public enum MavenModule {
    ROOT(LOMBOK, MAPSTRUCT) {
        @Override
        public PomModel enrich(ApiWiserBundle bundle) {
            final var modules = bundle.modules();
            final var projectDependencies = new ArrayList<>(asList(MavenProjectHelper.dependencies(bundle.mavenDependencyVersions())));
            final var properties = new Properties();
            properties.setProperty(ApiWiserFinals.moduleNameProperty(), ApiWiserFinals.ROOT);
            for (final var dependency : projectDependencies) {
                final var propertyMame = dependency.getArtifactId() + DOT_VERSION;
                properties.setProperty(propertyMame, dependency.getVersion());
                dependency.setVersion(format("${%s}", propertyMame));
                if (MavenLib.of(dependency).filter(MavenLib::isBOM).isPresent()) {
                    dependency.setType(POM);
                    dependency.setScope(IMPORT);
                }
            }
            MAVEN_DEPENDENCY_VERSIONS.forEach((k, v) -> {
                final var propertyMame = k.getArtifactId() + DOT_VERSION;
                if (properties.containsKey(propertyMame)) {
                    return;
                }
                final var dependency = new Dependency();
                dependency.setGroupId(k.getGroupId());
                dependency.setArtifactId(k.getArtifactId());
                dependency.setVersion(format("${%s}", propertyMame));
                if (MavenLib.of(dependency).filter(MavenLib::isBOM).isPresent()) {
                    dependency.setType(POM);
                    dependency.setScope(IMPORT);
                }
                properties.setProperty(propertyMame, v);
                projectDependencies.add(dependency);
            });

            // Modules
            final var templateModules = ApiWiserTemplates.modules();
            return super.enrich(bundle)
                    .dependenciesManagements(projectDependencies.toArray(new Dependency[0]))
                    .properties(properties)
                    .modules(orderByDependency(templateModules)
                            .stream()
                            .filter(m -> !m.equals(ApiWiserFinals.ROOT))
                            .filter(m -> modules.isEmpty() || modules.contains(m))
                            .map(v -> bundle.artifactId() + DASH + v)
                            .toList()
                            .toArray(String[]::new));
        }
    },
    API {
        @Override
        public PomModel enrich(ApiWiserBundle pom) {
            return decorateAsModule(super.enrich(pom))
                    .dependency(pom.groupId(), pom.artifactId() + DASH + MODEL.moduleName());
        }
    },
    //MODEL(JACKSON_DATABIND) {
    MODEL(JACKSON_ANNOTATION) {
        @Override
        public PomModel enrich(ApiWiserBundle pom) {
            return decorateAsModule(super.enrich(pom));
        }
    },
    BIZ {
        @Override
        public PomModel enrich(ApiWiserBundle pom) {
            return decorateAsModule(super.enrich(pom))
                    .dependency(pom.groupId(), pom.artifactId() + DASH + API.moduleName());
        }
    },
    SPRING_APP(SPRING_BOOT_STARTER_WEB) {
        @Override
        public PomModel enrich(ApiWiserBundle bundle) {
            return decorateAsModule(super.enrich(bundle))
                    .dependency(bundle.groupId(), bundle.artifactId() + DASH + BIZ.moduleName())
                    .dependency(SPRING_BOOT_STARTER_TEST, MvnScope.TEST);
        }
    };

    static final Set<MavenLib> providedCoordinates = Set.of(LOMBOK, MAPSTRUCT);

    final MavenLib[] dependencies;

    MavenModule(MavenLib... coords) {
        dependencies = coords;
    }

    public List<Dependency> dependencies() {
        return Stream.of(dependencies).map(v -> {
            final var d = new Dependency();
            d.setGroupId(v.getGroupId());
            d.setArtifactId(v.getArtifactId());
            if (providedCoordinates.contains(v)) {
                d.setScope("provided");
            }
            return d;
        }).toList();
    }

    public PomModel enrich(ApiWiserBundle bundle) {
        return toPomModelBuilder(bundle).dependencies(this.dependencies().toArray(new Dependency[0]));
    }

    public PomModel decorateAsModule(PomModel pomModel) {
        final var parentArtifactId = pomModel.artifactId();
        final var parentGroupId = pomModel.groupId();
        final var parentVersionId = pomModel.version();
        return pomModel
                .property(ApiWiserFinals.moduleNameProperty(), this.moduleName())
                .parentGroupId(parentGroupId)
                .parentArtifactId(parentArtifactId)
                .parentVersion(parentVersionId)
                .groupId(null)
                .artifactId(parentArtifactId + '-' + moduleName())
                .version(null);
    }

    public String moduleName() {
        return name().toLowerCase().replace('_', '-');
    }

    public static List<String> orderByDependency(Map<String, Set<String>> modules) {
        checkCrossDependency(modules);
        final var result = new ArrayList<>(modules.keySet());
        int counter;
        do {
            counter = 0;
            for (final var key : new ArrayList<>(result)) {
                int index = -1;
                for (final var d : modules.get(key)) {
                    final var i = result.indexOf(d);
                    if (index < i) {
                        index = i;
                    }
                }
                if (counter < index) {
                    result.remove(counter);
                    result.add(index, key);
                    break;
                }
                counter++;
            }
        } while (counter < result.size());
        return result;
    }

    static void checkCrossDependency(Map<String, Set<String>> modules) {
        for (final var key : modules.keySet()) {
            final var chain = new ArrayList<String>();
            chain.add(key);
            if (isDependencyMutual(modules, chain)) {
                throw CircularDependencyException.create(chain);
            }
        }
    }

    static boolean isDependencyMutual(Map<String, Set<String>> modules, List<String> chain) {
        for (final var d : modules.get(chain.get(chain.size()-1))) {
            if (chain.contains(d)) {
                chain.add(d);
                return true;
            }
            final var c = new ArrayList<>(chain);
            c.add(d);
            if (isDependencyMutual(modules, c)) {
                chain.clear();
                chain.addAll(c);
                return true;
            }
        }
        return false;
    }

    static final class CircularDependencyException extends RuntimeException {
        CircularDependencyException(String message) {
            super(message);
        }

        static CircularDependencyException create(List<String> modules) {
            return new CircularDependencyException("Circular Dependency violation: " + String.join(" -> ", modules));
        }
    }

}
