package org.metalib.wiser.api.template.jackson.http.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonHttpClientTemplateBuilder {
    static String HTTP = "http";
    static String CLIENT = "client";

    static String MODULE_NAME = HTTP + DASH + CLIENT;
}
