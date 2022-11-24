package org.example.rest.resources.oauth2;

import static java.util.Objects.requireNonNull;

public enum TokenType {
    BOT("Bot"),
    BEARER("Bearer");

    private final String value;

    public static TokenType create(String value) {
        switch (requireNonNull(value)) {
            case "Bot":
                return BOT;
            case "Bearer":
                return BEARER;
            default:
                throw new IllegalArgumentException();
        }
    }

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
