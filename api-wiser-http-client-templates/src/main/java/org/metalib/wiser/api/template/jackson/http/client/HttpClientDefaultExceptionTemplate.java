package org.metalib.wiser.api.template.jackson.http.client;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import lombok.Getter;
import org.metalib.wiser.api.template.ApiWiserBundle;
import org.metalib.wiser.api.template.ApiWiserConfig;
import org.metalib.wiser.api.template.ApiWiserTargetFile;
import org.metalib.wiser.api.template.ApiWiserTemplateService;

import javax.lang.model.element.Modifier;
import java.io.File;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;
import static org.metalib.wiser.api.template.ApiWiserFinals.DOT;
import static org.metalib.wiser.api.template.ApiWiserFinals.JAVA;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.CLIENT;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.HTTP;
import static org.metalib.wiser.api.template.jackson.http.client.JacksonHttpClientTemplateBuilder.MODULE_NAME;

/**
 * Template service for generating a ResponseWrapper class.
 * 
 * <p>This template generates a generic wrapper class for HTTP responses that can hold
 * either a typed body (deserialized from JSON) or raw text content. The wrapper is used
 * by the HTTP client to handle API responses in a consistent way.</p>
 */
public class HttpClientDefaultExceptionTemplate implements ApiWiserTemplateService {

    /** The name of this template */
    public static final String TEMPLATE_NAME = "http-client-default-exception";

    /** The unique identifier for this template */
    public static final String TEMPLATE_ID = "api-wiser::" + TEMPLATE_NAME;

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
     * @return "java" as the file extension
     */
    @Override
    public String fileExtension() {
        return JAVA;
    }

    /**
     * Defines the target file location for the generated ResponseWrapper class.
     * 
     * @param config the API Wiser configuration
     * @return the target file information
     */
    @Override
    public ApiWiserTargetFile targetFile(ApiWiserConfig config) {
        return new ApiWiserTargetFile() {
            @Override
            public String relativeFolder() {
                return config.mavenArtifactId()
                        + DASH
                        + MODULE_NAME
                        + File.separator + config.generatedSourceFolder()
                        + File.separator + config.basePackage().replace(DOT, File.separator)
                        + File.separator + HTTP
                        + File.separator + CLIENT;
            }

            @Override
            public String fileName() {
                return LOWER_HYPHEN.to(UPPER_CAMEL, TEMPLATE_NAME);
            }
        };
    }

    /**
     * Generates the Java code for the ResponseWrapper class.
     * 
     * @param bundle the API Wiser bundle containing configuration and context
     * @return the generated Java code as a string
     */
    @Override
    public String toText(ApiWiserBundle bundle) {
        final var jacksonBodyHandlerPackage = bundle.basePackage() + DOT + HTTP + DOT + CLIENT;
        return JavaFile.builder(jacksonBodyHandlerPackage, httpDefaultExceptionTypeBuilder().build())
                .build().toString();
    }

    static TypeSpec.Builder httpDefaultExceptionTypeBuilder() {
        final var className = LOWER_HYPHEN.to(UPPER_CAMEL, TEMPLATE_NAME);
        return TypeSpec
                .classBuilder(className)
                .superclass(TypeVariableName.get("RuntimeException"))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec
                        .constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(String.class, "message").build())
                        .addCode(CodeBlock.builder().addStatement("super(message)").build())
                        .build())
                ;
    }
}
