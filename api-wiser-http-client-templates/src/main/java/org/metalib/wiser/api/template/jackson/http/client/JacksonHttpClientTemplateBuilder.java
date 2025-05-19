package org.metalib.wiser.api.template.jackson.http.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;

/**
 * Utility class that provides constants for the HTTP client templates.
 * 
 * <p>This class defines constants used by the other template classes in this package.
 * It includes constants for the module name and path components.</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonHttpClientTemplateBuilder {
    /** Constant for the "http" path component */
    static final String HTTP = "http";

    /** Constant for the "client" path component */
    static final String CLIENT = "client";

    /** The module name, constructed from HTTP and CLIENT constants */
    static final String MODULE_NAME = HTTP + DASH + CLIENT;
}
