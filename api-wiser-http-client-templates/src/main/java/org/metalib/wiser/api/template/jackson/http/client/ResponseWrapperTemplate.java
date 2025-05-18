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

public class ResponseWrapperTemplate implements ApiWiserTemplateService {

    public static final String TEMPLATE_NAME = "response-wrapper";
    public static final String TEMPLATE_ID = "api-wiser::" + TEMPLATE_NAME;

    @Override
    public String id() {
        return TEMPLATE_ID;
    }

    @Override
    public String moduleName() {
        return MODULE_NAME;
    }

    @Override
    public boolean isSupportingFile() {
        return true;
    }

    @Override
    public String fileExtension() {
        return JAVA;
    }

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

    @Override
    public String toText(ApiWiserBundle bundle) {
        final var jacksonBodyHandlerPackage = bundle.basePackage() + DOT + HTTP + DOT + CLIENT;
        return JavaFile.builder(jacksonBodyHandlerPackage, genericResponseTypeBuilder().build())
                .build().toString();
    }

    // https://stackoverflow.com/questions/57629401/deserializing-json-using-java-11-httpclient-and-custom-bodyhandler-with-jackson
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
