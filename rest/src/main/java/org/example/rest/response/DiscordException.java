package org.example.rest.response;

public class DiscordException extends RuntimeException {
    private final ErrorResponse response;

    public DiscordException(ErrorResponse response) {
        this.response = response;
    }
}
