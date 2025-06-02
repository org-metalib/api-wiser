package org.metalib.wiser.api.template.model.jackson;

import com.palantir.javapoet.TypeSpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.metalib.wiser.api.template.ApiWiserBundle;

import static org.metalib.wiser.api.template.model.jackson.JacksonModelBuilder.buildModelClass;
import static org.metalib.wiser.api.template.model.jackson.JacksonModelBuilder.buildModelEnum;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelBuilder {

  public static TypeSpec.Builder createModelBuilder(ApiWiserBundle openApiModelOpt) {
    return openApiModelOpt.codeModel().filter(ApiWiserBundle.CodeModel::isEnum)
        .map(v -> buildModelEnum(openApiModelOpt))
        .orElseGet(() -> buildModelClass(openApiModelOpt));
  }
}
