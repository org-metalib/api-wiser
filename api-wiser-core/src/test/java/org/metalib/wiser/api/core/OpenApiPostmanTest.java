package org.metalib.wiser.api.core;

import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MAVEN_DEPENDENCIES_NAME;
import static org.metalib.wiser.api.core.java.code.ApiWiserConst.X_API_WISER_MODULES;
import static org.metalib.wiser.api.template.ApiWiserFinals.API;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.metalib.wiser.api.core.java.code.ApiWiserCode;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

public class OpenApiPostmanTest {

  static Class<OpenApiPostmanTest> CLASS = OpenApiPostmanTest.class;

  static List<String> generatorNames = List.of(
      JAVA,
      "jaxrs-cxf-client",
      "java-helidon-client",
      "java-helidon-server",
      "java-inflector",
      "java-micronaut-client",
      "java-micronaut-server",
      "java-msf4j",
      "java-pkmst",
      "java-play-framework",
      "java-undertow-server",
      "java-vertx",
      "java-vertx-web",
      "java-camel",
      "jaxrs-cxf",
      "jaxrs-cxf-extended",
      "jaxrs-cxf-cdi",
      "jaxrs-jersey",
      "jaxrs-resteasy",
      "jaxrs-resteasy-eap",
      "jaxrs-spec",
      "scalatra",
      "scala-akka",
      "scala-akka-http-server",
      "scala-finch",
      "scala-gatling",
      "scala-lagom-server",
      "scala-play-server",
      "scala-sttp",
      "scalaz",
      "spring"
  );

  @Test
  void testJavaGenerator0() {
    final var dryRun = false;
    final var generatorName = "jaxrs-cxf-cdi";
    final var configurator = new CodegenConfigurator()
        .setInstantiationTypes(Map.of())
        .setGeneratorName(generatorName)
        .setOutputDir("target/openapi/" + generatorName)
        .setInputSpec("target/test-classes/spec-test/openapi.yaml")
        ;
    new DefaultGenerator(dryRun)
        .opts(configurator.toClientOptInput())
        .generate();
  }

  @SneakyThrows
  @Test
  @Disabled
  void testJavaGenerator() {
    final var dryRun = false;

    for (final var generatorName : generatorNames) {
      final var configurator = new CodegenConfigurator()
          .setInstantiationTypes(Map.of())
          .setGeneratorName(generatorName)
          .setOutputDir("target/openapi/" + generatorName)
          .setInputSpec(URLDecoder.decode(Objects.requireNonNull(CLASS
                  .getResource("/spec-test/openapi.yaml")).getFile(),
                  StandardCharsets.UTF_8))
          ;
      new DefaultGenerator(dryRun)
          .opts(configurator.toClientOptInput())
          .generate();
    }
  }

  @Test
  void testOpenApiGenerator() {
    ApiWiserCode.builder()
        .dry(false)
        .packageName("org.metalib.postman.collection")
        .apiPackageName("org.metalib.postman.collection.api")
        .modelPackageName("org.metalib.postman.collection.model")
        .outputDir(new File(System.getProperty("user.dir"), "target/openapi-custom"))
        .inputSpec(new File(URLDecoder.decode(CLASS.getResource("/spec-test/openapi.yaml").getFile(), StandardCharsets.UTF_8)))
        .mavenGroupId("org.metalib.openapi.buddy.generator")
        .mavenArtifactId("api-wiser-test")
        .mavenVersion("0.0.1-SNAPSHOT")
        .additionalProperty(X_API_WISER_MAVEN_DEPENDENCIES_NAME, List.of(
            "org.projectlombok:lombok:1.18.30",
            //"org.mapstruct:mapstruct:1.5.5.Final",
            "com.fasterxml.jackson:jackson-bom:2.15.2",
            "org.springframework:spring-framework-bom:5.3.29",
            "org.springframework.boot:spring-boot-dependencies:2.7.14",
            "io.netty:netty-bom:4.1.96.Final",
            "io.github.openfeign:feign-bom:12.4",
            "com.squareup.okhttp3:okhttp-bom:4.11.0",
            "com.squareup.retrofit2:retrofit:2.9.0",
            "com.squareup.retrofit2:adapter-rxjava2:2.9.0",
            "com.squareup.retrofit2:adapter-rxjava3:2.9.0",
            "com.squareup.retrofit2:converter-jackson:2.9.0",
            "com.squareup.retrofit2:converter-scalars:2.9.0",
            "com.squareup.retrofit2:converter-protobuf:2.9.0"
        ))
        .build().generator().generate();
  }


  @Test
  void testIBKRopenapi() {
    ApiWiserCode.builder()
            .dry(false)
            .packageName("org.metalib.market.data.ibkr")
            .apiPackageName("org.metalib.market.data.ibkr.api")
            .modelPackageName("org.metalib.market.data.ibkr.model")
            .outputDir(new File(System.getProperty("user.dir"), "target/openapi-ibkr"))
            .inputSpec(new File(URLDecoder.decode(CLASS.getResource("/spec-test/ibkr-openapi-3.yaml").getFile(),
                    StandardCharsets.UTF_8)))
            .mavenGroupId("org.metalib.market.data.ibkr")
            .mavenArtifactId("ibkr-client-gateway")
            .mavenVersion("0.0.1-SNAPSHOT")
            .additionalProperty(X_API_WISER_MAVEN_DEPENDENCIES_NAME, List.of(
                    "org.projectlombok:lombok:1.18.30",
                    //"org.mapstruct:mapstruct:1.5.5.Final",
                    "com.fasterxml.jackson:jackson-bom:2.15.2",
                    "org.springframework:spring-framework-bom:5.3.29",
                    "org.springframework.boot:spring-boot-dependencies:2.7.14",
                    "io.netty:netty-bom:4.1.96.Final",
                    "io.github.openfeign:feign-bom:12.4",
                    "com.squareup.okhttp3:okhttp-bom:4.11.0",
                    "com.squareup.retrofit2:retrofit:2.9.0",
                    "com.squareup.retrofit2:adapter-rxjava2:2.9.0",
                    "com.squareup.retrofit2:adapter-rxjava3:2.9.0",
                    "com.squareup.retrofit2:converter-jackson:2.9.0",
                    "com.squareup.retrofit2:converter-scalars:2.9.0",
                    "com.squareup.retrofit2:converter-protobuf:2.9.0"
            ))
            .additionalProperty(X_API_WISER_MODULES, List.of(
                    "model",
                    "api",
                    "biz",
                    "http-client",
                    "spring-app"))
            .build().generator().generate();
  }

  @Test
  void testFastHttpBin() {
    ApiWiserCode.builder()
            .dry(false)
            .packageName("org.metalib.bin.http.fast")
            .apiPackageName("org.metalib.bin.http.fast.api")
            .modelPackageName("org.metalib.bin.http.fast.model")
            .outputDir(new File(System.getProperty("user.dir"), "target/fast-http-bin"))
            .inputSpec(new File(URLDecoder.decode(CLASS.getResource("/spec-test/fast-api-http-bin-openapi-3.yaml").getFile(),
                    StandardCharsets.UTF_8)))
            .mavenGroupId("org.metalib.bin.http.fast")
            .mavenArtifactId("fast-http-bin")
            .mavenVersion("0.0.1-SNAPSHOT")
            .additionalProperty(X_API_WISER_MAVEN_DEPENDENCIES_NAME, List.of(
                    "org.projectlombok:lombok:1.18.30",
                    //"org.mapstruct:mapstruct:1.5.5.Final",
                    "com.fasterxml.jackson:jackson-bom:2.15.2",
                    "org.springframework:spring-framework-bom:5.3.29",
                    "org.springframework.boot:spring-boot-dependencies:2.7.14",
                    "io.netty:netty-bom:4.1.96.Final",
                    "io.github.openfeign:feign-bom:12.4",
                    "com.squareup.okhttp3:okhttp-bom:4.11.0",
                    "com.squareup.retrofit2:retrofit:2.9.0",
                    "com.squareup.retrofit2:adapter-rxjava2:2.9.0",
                    "com.squareup.retrofit2:adapter-rxjava3:2.9.0",
                    "com.squareup.retrofit2:converter-jackson:2.9.0",
                    "com.squareup.retrofit2:converter-scalars:2.9.0",
                    "com.squareup.retrofit2:converter-protobuf:2.9.0"
            ))
            .additionalProperty(X_API_WISER_MODULES, List.of(API))
//                    "model",
//                    "api",
//                    "biz",
//                    "http-client",
//                    "spring-app"))
            .build().generator().generate();
  }

  @SneakyThrows
  static String string(final String resourcePath) {
    try (final var inputStream = CLASS.getResourceAsStream(resourcePath)) {
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}

