package org.openapitools.model;

import javax.validation.constraints.*;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum OuterEnum {

    @JsonProperty("placed") PLACED(String.valueOf("placed")), @JsonProperty("approved") APPROVED(String.valueOf("approved")), @JsonProperty("delivered") DELIVERED(String.valueOf("delivered"));


    private String value;

    OuterEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static OuterEnum fromValue(String value) {
        for (OuterEnum b : OuterEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}


