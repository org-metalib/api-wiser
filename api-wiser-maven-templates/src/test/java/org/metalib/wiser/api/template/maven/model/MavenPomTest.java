package org.metalib.wiser.api.template.maven.model;

import org.junit.jupiter.api.Test;
import org.metalib.wiser.api.template.maven.model.PomModel;

import java.util.Map;

import static org.metalib.wiser.api.template.maven.model.MavenLib.JACKSON_BOM;
import static org.metalib.wiser.api.template.maven.model.MavenLib.LOMBOK;
import static org.metalib.wiser.api.template.maven.model.MavenLib.MAPSTRUCT;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_BOOT_BOM;
import static org.metalib.wiser.api.template.maven.model.MavenLib.SPRING_FRAMEWORK_BOM;

class MavenPomTest {

  @Test
  void test() {

    final var versions = Map.of(
        LOMBOK, "1.18.28",
        MAPSTRUCT, "1.5.5.Final",
        JACKSON_BOM, "2.15.2",
        SPRING_FRAMEWORK_BOM, "5.3.29",
        SPRING_BOOT_BOM, "2.7.14");

    final var pom = PomModel.builder()
        .groupId("org.metalib.openapi.buddy.generator")
        .artifactId("api-wiser-test")
        .version("0.0.1-SNAPSHOT")
        .build()
        ;
    //final var rootXml = ROOT.enrich(pom).toXml();

    return;
  }
}
