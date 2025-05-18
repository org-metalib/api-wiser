package org.metalib.wiser.api.template;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiWiserTemplatesTest {

    @Test
    void apiWiserTemplatesShouldNotBeEmpty() {
        final var templates = ApiWiserTemplates.list();
        assertNotNull(templates);
        assertFalse(templates.isEmpty());
    }

}