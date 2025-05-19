package org.openapitools.model;

import javax.validation.constraints.*;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum OuterEnumDefaultValue {

    @JsonProperty("placed") PLACED(String.valueOf("placed")), @JsonProperty("approved") APPROVED(String.valueOf("approved")), @JsonProperty("delivered") DELIVERED(String.valueOf("delivered"));


    private String value;

    OuterEnumDefaultValue(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static OuterEnumDefaultValue fromValue(String value) {
        for (OuterEnumDefaultValue b : OuterEnumDefaultValue.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}


