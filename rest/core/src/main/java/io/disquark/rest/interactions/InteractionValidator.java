package io.disquark.rest.interactions;

public abstract class InteractionValidator {
    protected final String verifyKey;

    protected InteractionValidator(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public abstract boolean validate(String timestamp, String body, String signature);
}
