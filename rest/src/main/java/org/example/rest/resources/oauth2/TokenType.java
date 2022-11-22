package org.example.rest.resources.oauth2;

public enum TokenType {
    BOT("Bot"),
    BEARER("Bearer");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
