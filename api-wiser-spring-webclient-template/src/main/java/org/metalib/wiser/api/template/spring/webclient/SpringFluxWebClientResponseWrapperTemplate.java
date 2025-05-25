package org.metalib.wiser.api.template.spring.webclient;

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
import static org.metalib.wiser.api.template.spring.webclient.SpringFluxWebClientTemplateBuilder.MODULE_NAME;

public class SpringFluxWebClientResponseWrapperTemplate implements ApiWiserTemplateService {

    /** The name of this template */
    public static final String TEMPLATE_NAME = "spring-flux-webclient-response-wrapper";

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
                        + File.separator + MODULE_NAME.replace(DOT, File.separator);
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
        final var modulePackageName = bundle.basePackage() + DOT + MODULE_NAME.replace(DASH, DOT);
        return JavaFile.builder(modulePackageName, genericResponseTypeBuilder().build()).build().toString();
    }

    /**
     * Builds the TypeSpec for the ResponseWrapper class.
     * 
     * <p>The ResponseWrapper is a generic class that can hold either a typed body (deserialized from JSON)
     * or raw text content. It has two constructors: one for typed bodies and one for raw byte content.</p>
     * 
     * <p>Based on: https://stackoverflow.com/questions/57629401/deserializing-json-using-java-11-httpclient-and-custom-bodyhandler-with-jackson</p>
     * 
     * @return the TypeSpec builder for the ResponseWrapper class
     */
    static TypeSpec.Builder genericResponseTypeBuilder() {
        final var typeVariableW = TypeVariableName.get("T");
        final var className =LOWER_HYPHEN.to(UPPER_CAMEL, TEMPLATE_NAME);
        return TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariable(typeVariableW)
                .addAnnotation(Getter.class)
                .addField(typeVariableW, "body", Modifier.PRIVATE)
                .addField(String.class, "text", Modifier.PRIVATE)
                .addMethod(MethodSpec
                        .constructorBuilder()
                        .addParameter(ParameterSpec.builder(typeVariableW, "body").build())
                        .addCode(CodeBlock.builder().addStatement("this.body = body").build())
                        .build())
                .addMethod(MethodSpec
                        .constructorBuilder()
                        .addParameter(ParameterSpec.builder(byte[].class, "content").build())
                        .addCode(CodeBlock.builder().addStatement("this.text = new String(content)").build())
                        .build())
                ;
    }
}
