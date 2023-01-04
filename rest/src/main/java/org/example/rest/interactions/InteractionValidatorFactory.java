package org.example.rest.interactions;

import io.vertx.mutiny.core.buffer.Buffer;

import java.util.function.Function;

public interface InteractionValidatorFactory extends Function<String, InteractionValidator> {

    InteractionValidatorFactory NO_OP = new InteractionValidatorFactory() {
        @Override
        public InteractionValidator apply(String s) {
            return new InteractionValidator(s) {
                @Override
                public boolean validate(Buffer timestamp, Buffer body, Buffer signature) {
                    return true;
                }
            };
        }
    };
}
