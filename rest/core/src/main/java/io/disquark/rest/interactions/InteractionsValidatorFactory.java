package io.disquark.rest.interactions;

import java.util.function.Function;

public interface InteractionsValidatorFactory extends Function<String, InteractionsValidator> {

    InteractionsValidatorFactory NO_OP = new InteractionsValidatorFactory() {
        @Override
        public InteractionsValidator apply(String s) {
            return new InteractionsValidator(s) {
                @Override
                public boolean validate(String timestamp, String body, String signature) {
                    return true;
                }
            };
        }
    };
}
