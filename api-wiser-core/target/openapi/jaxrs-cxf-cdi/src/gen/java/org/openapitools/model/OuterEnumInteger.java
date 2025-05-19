package org.openapitools.model;

import javax.validation.constraints.*;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum OuterEnumInteger {

    @JsonProperty("0") NUMBER_0(Integer.valueOf(0)), @JsonProperty("1") NUMBER_1(Integer.valueOf(1)), @JsonProperty("2") NUMBER_2(Integer.valueOf(2));


    private Integer value;

    OuterEnumInteger(Integer v) {
        value = v;
    }

    public Integer value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static OuterEnumInteger fromValue(Integer value) {
        for (OuterEnumInteger b : OuterEnumInteger.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}


