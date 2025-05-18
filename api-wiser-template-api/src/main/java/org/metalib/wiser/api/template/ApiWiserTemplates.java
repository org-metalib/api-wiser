package org.metalib.wiser.api.template;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ApiWiserTemplates {

    static ServiceLoader<ApiWiserTemplateService> apiWiserServices = ServiceLoader.load(ApiWiserTemplateService.class,
            ApiWiserTemplateService.class.getClassLoader());

    public static List<ApiWiserTemplateService> list() {
        return apiWiserServices
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    public static boolean anyModuleOutput(String module) {
        return apiWiserServices
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(v -> v.moduleName().equals(module))
                .anyMatch(v -> v.isModelFile() || v.isApiFile() || v.isSupportingFile());
    }

    public static ApiWiserTemplateService retrieve(String id) {
        final var loader = ServiceLoader.load(ApiWiserTemplateService.class, ApiWiserTemplateService.class.getClassLoader());

        final var templateServiceList = new StringBuilder();
        for (final var templateService : loader) {
            if (id.equals(templateService.id())) {
                return templateService;
            }
            templateServiceList.append(templateService.id()).append("\n");
        }
        try {
            // Attempt to load skipping SPI
            return (ApiWiserTemplateService) Class.forName(id).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ApiWiserTemplateClassLoadException(format(Locale.ROOT,
                    "Couldn't load template service %s. Available options: %n%s", id, templateServiceList), e);
        }
    }

    public static class ApiWiserTemplateClassLoadException extends RuntimeException {
        ApiWiserTemplateClassLoadException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}
