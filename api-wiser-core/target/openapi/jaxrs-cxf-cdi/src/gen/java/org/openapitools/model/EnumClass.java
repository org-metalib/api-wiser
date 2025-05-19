package org.openapitools.model;

import javax.validation.constraints.*;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum EnumClass {

    @JsonProperty("_abc") _ABC(String.valueOf("_abc")), @JsonProperty("-efg") _EFG(String.valueOf("-efg")), @JsonProperty("(xyz)") _XYZ_(String.valueOf("(xyz)"));


    private String value;

    EnumClass(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static EnumClass fromValue(String value) {
        for (EnumClass b : EnumClass.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}


