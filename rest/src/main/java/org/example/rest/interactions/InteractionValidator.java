package org.example.rest.interactions;

import io.vertx.mutiny.core.buffer.Buffer;

public abstract class InteractionValidator {
    protected final String verifyKey;

    protected InteractionValidator(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public abstract boolean validate(Buffer timestamp, Buffer body, Buffer signature);
}
