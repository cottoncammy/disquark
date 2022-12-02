package org.example.rest.util;

import com.fasterxml.jackson.annotation.JsonValue;

public interface FlagEnum {

    @JsonValue
    int getValue();
}
