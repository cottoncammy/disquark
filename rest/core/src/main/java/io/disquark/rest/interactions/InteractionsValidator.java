package io.disquark.rest.interactions;

public abstract class InteractionsValidator {
    protected final String verifyKey;

    protected InteractionsValidator(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public abstract boolean validate(String timestamp, String body, String signature);
}
