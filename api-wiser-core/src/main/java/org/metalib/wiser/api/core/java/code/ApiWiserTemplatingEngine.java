package org.metalib.wiser.api.core.java.code;

import org.metalib.wiser.api.template.ApiWiserTemplates;
import org.metalib.wiser.api.template.ApiWiserTemplateService;
import org.metalib.wiser.api.core.java.ApiWiserBundleModel;
import org.openapitools.codegen.api.TemplatingEngineAdapter;
import org.openapitools.codegen.api.TemplatingExecutor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

/**
 * A templating engine adapter for the API Wiser system that implements the OpenAPI Tools
 * TemplatingEngineAdapter interface.
 * <p>
 * This class is responsible for:
 * - Loading and managing templates from ApiWiserTemplates
 * - Determining which files the engine can handle
 * - Compiling templates using the appropriate template service
 * <p>
 * The engine is used by {@link ApiWiserGenerator} to process templates during code generation.
 * It delegates the actual template processing to the appropriate {@link ApiWiserTemplateService}
 * implementation based on the template ID.
 */
public class ApiWiserTemplatingEngine implements TemplatingEngineAdapter {

    public static final String ENGINE_ID = "java-poet-templating-engine";

    static final Set<String> TEMPLATES = new HashSet<>();
    static final Map<String, ApiWiserTemplateService> TEMPLATE_SERVICES = new HashMap<>();
    static final String[] EXTENSIONS;

    static {
        final var templates = ApiWiserTemplates.list();
        TEMPLATE_SERVICES.putAll(templates.stream().collect(Collectors.toMap(ApiWiserTemplateService::id, v -> v)));

        TEMPLATES.addAll(templates.stream().map(ApiWiserTemplateService::id).collect(toSet()));

        EXTENSIONS = templates.stream().map(ApiWiserTemplateService::fileExtension).distinct().toArray(String[]::new);
    }

    @Override
    public String getIdentifier() {
        return ENGINE_ID;
    }

    @Override
    public String[] getFileExtensions() {
        return EXTENSIONS;
    }

    @Override
    public boolean handlesFile(String filename) {
        return TEMPLATES.contains(filename);
    }

    @Override
    public String compileTemplate(TemplatingExecutor executor,
                                  Map<String, Object> bundleProperties,
                                  String templateId) {
        final var bundle = ApiWiserBundleModel.wrap(bundleProperties);
        final var template = TEMPLATE_SERVICES.get(templateId);
        if (null == template) {
            throw new IllegalArgumentException(format("Template <%s> not implemented", templateId));
        } else if (bundle.module().equals(template.moduleName())) {
            return template.toText(bundle);
        } else {
            return null;
        }
    }
}
