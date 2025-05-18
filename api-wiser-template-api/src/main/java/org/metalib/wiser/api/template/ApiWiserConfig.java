package org.metalib.wiser.api.template;

import java.io.File;

public interface ApiWiserConfig {
    String mavenGroupId();

    String mavenArtifactId();

    String mavenVersion();

    String sourceFolder();

    String generatedSourceFolder();

    public String generatedResourceFolder();

    String basePackage();

    String baseEntityName();

    String camelizeBaseEntityName();

    String toModelFilename(String modelTag);

    String[] modules();

    File projectDir();

    File projectBuildDir();
}