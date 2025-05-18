package org.metalib.wiser.api.template;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.metalib.wiser.api.template.ApiWiserFinals.API_WISER_VERSION;

class ApiWiserFinalsTest {

    @Test
    void testApiWiserVersionShouldSucceed() {
        assertEquals(API_WISER_VERSION, System.getProperty("api-wiser.version"));
    }
}