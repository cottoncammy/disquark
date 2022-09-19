package org.example.rest.request;

public enum TokenType {
    BOT("Bot"),
    BEARER("Bearer");

    private final String value;

    TokenType(String value) {
        this.value = value;
    }
}
