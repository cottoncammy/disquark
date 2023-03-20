package io.disquark.rest.json.forum;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SortOrderType {
    @JsonEnumDefaultValue
    UNKNOWN(-1),
    LATEST_ACTIVITY(0),
    CREATION_DATE(1);

    private final int value;

    SortOrderType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
