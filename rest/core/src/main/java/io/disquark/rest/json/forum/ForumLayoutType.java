package io.disquark.rest.json.forum;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ForumLayoutType {
    @JsonEnumDefaultValue
    UNKNOWN(-1),
    NOT_SET(0),
    LIST_VIEW(1),
    GALLERY_VIEW(2);

    private final int value;

    ForumLayoutType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
