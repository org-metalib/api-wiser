package org.metalib.wiser.api.template;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ApiWiserFinals {

    public static final String ROOT = "root";
    public static final String MODULE = "module";
    public static final String API = "api";
    public static final String BIZ = "biz";
    public static final String MODEL = "model";
    public static final String README = "README";
    public static final String ST_EXT = "st";
    public static final String JAVA = "java";
    public static final String JAR = "jar";
    public static final String POM = "pom";
    public static final String MD = "md";
    public static final String TXT = "txt";
    public static final String XML = "xml";
    public static final String JSON = "json";
    public static final String YAML = "yaml";
    public static final String TEMPLATES = "templates";

    public static final String BLANK = "";
    public static final String COLON = ":";
    public static final String DOUBLE_COLON = "::";
    public static final String DASH = "-";
    public static final String ARRAY = "array";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static final String LF = "\n";
    public static final String CR = "\r";
    public static final String POM_MODEL_VERSION = "4.0.0";
    public static final String POM_MODEL_ENCODING = "UTF-8";
    public static final int INDEX_NOT_FOUND = -1;

    public static final String API_WISER = "api-wiser";
    public static final String X_API_WISER = "x-" + API_WISER;
    public static final String X_API_WISER_TARGET_FILE = X_API_WISER + "-target-file";
    public static final String X_API_WISER_GENERATED_RESOURCE_FOLDER = X_API_WISER + "-generated-resource-folder";
    public static final String X_API_WISER_SOURCE_FOLDER = X_API_WISER + "-source-folder";
    public static final String X_API_WISER_GENERATED_SOURCE_FOLDER = X_API_WISER + "-generated-source-folder";
    public static final String X_API_WISER_API_PACKAGE = X_API_WISER + "-generated-api-package";
    public static final String X_API_WISER_BASE_ENTITY_NAME = X_API_WISER + "-base-entity-name";
    public static final String X_API_WISER_CONTEXT = X_API_WISER + "-context";
    private static final Properties API_WISER_META = loadMeta();
    public static final String API_WISER_VERSION = API_WISER_META.getProperty("api-wiser.version");

    public static String moduleNameProperty() {
        return API_WISER + DOT + MODULE;
    }

    public static String prefix(String property) {
        return API_WISER + DOT + property;
    }

    @SneakyThrows
    public static String textFromResource(final String resourcePath) {
        try (final var inputStream = ApiWiserFinals.class.getResourceAsStream(resourcePath)) {
            return new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @SneakyThrows
    static Properties loadMeta() {
        try (final var resources = ApiWiserFinals.class.getClassLoader().getResourceAsStream("api-wiser.meta.properties")) {
            final var result = new Properties();
            result.load(resources);
            return result;
        }
    }
}
