package org.metalib.wiser.api.template.spring.webclient;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.metalib.wiser.api.template.ApiWiserFinals.DASH;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebClientTemplateBuilder {

    static final String SPRING = "spring";
    static final String WEBCLIENT = "webclient";

    static final String MODULE_NAME = SPRING + DASH + WEBCLIENT;
}
