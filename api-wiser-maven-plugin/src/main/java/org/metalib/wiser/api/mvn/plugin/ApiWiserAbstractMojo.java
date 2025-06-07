package org.metalib.wiser.api.mvn.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.io.File;
import java.util.Optional;

import static java.lang.String.format;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.POM;
import static org.metalib.wiser.api.template.ApiWiserFinals.ROOT;

public abstract class ApiWiserAbstractMojo extends AbstractMojo {

    /**
     * The current Maven project
     */
    @Inject
    MavenProject mavenProject;

    /**
     * The base directory of the project
     */
    @Parameter( defaultValue = "${project.basedir}", property = "api-wiser.project-dir", required = true )
    File projectDir;

    /**
     * The build directory of the project
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "api-wiser.project-build-dir", required = true )
    File projectBuildDir;

    /**
     * The base package for the generated code
     */
    @Parameter( property = "api-wiser.project-package", required = true )
    String projectPackage;

    File apiFileDir(String wiserModule) {
        return switch (wiserModule) {
            case "", ROOT -> new File(projectDir, API);
            default -> new File(projectDir.getParent(), API);
        };
    }

    String moduleName() {
        // We are adopting Maven 4 concept of the root module.
        // If the current dir has ".mvn" subdir this module is the root module.
        final var result = Optional.of(mavenProject).map(MavenProject::getProperties).map(v -> v.getProperty("api-wiser.module")).orElse("");
        if (new File(mavenProject.getBasedir(), ".mvn").isDirectory()) {
            return ROOT.equals(result) ? ROOT : "";
        } else {
            // A module that inherits "api-wiser.module" property from the root is not managed by API Wiser
            return ROOT.equals(result) ? "" : result;
        }
    }

    boolean isRootModule() {
        return ROOT.equals(moduleName());
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

    File apiSpec(String wiserModule) throws MojoExecutionException {
        final var apiSpecName = POM.equals(mavenProject.getPackaging())
                ? mavenProject.getArtifactId()
                : Optional.of(mavenProject)
                .map(MavenProject::getParent)
                .map(MavenProject::getArtifact)
                .map(Artifact::getArtifactId).orElse(null);
        if (null == apiSpecName) {
            throw new MojoExecutionException(format("Open API spec cannot be determined for project <%s>.", mavenProject.getArtifactId()));
        }
        final var apiSpec = new File(apiFileDir(wiserModule), apiSpecName + ".yaml");
        if (apiSpec.canRead()) {
            return apiSpec;
        }
        throw new MojoExecutionException(format("Open API spec <%s> not found.", apiSpec));
    }
}
