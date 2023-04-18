package io.disquark.rest.json.oauth2;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TokenType {
    BOT("Bot"),
    BEARER("Bearer");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
