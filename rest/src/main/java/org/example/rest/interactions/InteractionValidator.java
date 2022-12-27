package org.example.rest.interactions;

@FunctionalInterface
public interface InteractionValidator {

    boolean validate(String timestamp, String body, String signature);
}
