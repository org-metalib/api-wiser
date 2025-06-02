package org.metalib.wiser.api.template;

import org.metalib.wiser.api.template.model.ApiWiserBeanContext;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * OpenApi Model
 */
public interface ApiWiserBundle {

    Map<String, Object> unwrap();
    String groupId();
    String artifactId();
    String artifactVersion();

    String basePackage();
    String bundlePackage();
    String baseName();
    String camelizeBaseName();
    CodeType apiInterfaceName();
    CodeType bizClassName();
    String className();
    Map<String, String> mavenDependencyVersions();
    ApiWiserBeanContext beanContext();
    Set<String> modules();
    String module();
    List<MavenDependency<?>> moduleDependencies();
    File projectDir();
    File projectBuildDir();

    File targetFile();

    String sourceFolder();
    String generatedSourceFolder();
    String generatedResourceFolder();
    String apiPackage();
    String baseEntityName();

    Optional<CodeModel> codeModel();
    Optional<CodeOperation> codeOperation();

    Set<String> imports();
    String commonPath();
    String pathPrefix();

    Map<String, Object> extraProperties();

    interface CodeModel {
        boolean isEnum();
        String name();
        String description();
        CodeDiscriminator discriminator();
        String dataType();
        List<EnumVar> enumVars();
        List<CodeProperty> vars();

        CodeModel parentModel();
        List<CodeModel> children();
    }

    interface EnumVar {
        String name();
        Object value();
    }

    interface CodeDiscriminator {
        String propertyName();
    }

    interface CodeOperation {
        String className();
        List<Op> operations();

        interface Op {
            String baseName();
            String operationId();
            String returnType();
            CodeProperty returnProperty();
            String httpMethod();
            String path();
            List<MediaType> consumes();
            List<MediaType> produces();

            List<CodeParameter> allParams();

            List<CodeResponse> responses();
        }
    }
    interface MediaType {
        String name();
    }
    interface CodeProperty {
        String containerType();
        String baseType();
        String complexType();
        String dataType();
        String datatypeWithEnum();
        String baseName();
        String enumName();
        List<EnumVar> enumVars();
        String name();
        String defaultValue();
        String description();
        CodeProperty items();
        boolean required();
        boolean isInnerEnum();
    }
    interface CodeParameter {
        String dataType();
        String baseName();
        String name();
        boolean isPathParam();
        boolean isQueryParam();
        boolean isBodyParam();
    }
    interface CodeType {
        String packageName();
        String className();
    }

    interface CodeResponse {
        String code();
        boolean is1xx();
        boolean is2xx();
        boolean is3xx();
        boolean is4xx();
        boolean is5xx();
    }
}
