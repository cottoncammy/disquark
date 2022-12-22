package org.example.rest.resources.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.example.rest.resources.Locale;

import java.util.Map;
import java.util.Optional;

public interface ApplicationRoleConnectionMetadata {

    static Builder builder() {
        return new Builder();
    }

    Type type();

    String key();

    String name();

    @JsonProperty("name_localizations")
    Optional<Map<Locale, String>> nameLocalizations();

    String description();

    @JsonProperty("description_localizations")
    Optional<Map<Locale, String>> descriptionLocalizations();

    enum Type {
        INTEGER_LESS_THAN_OR_EQUAL(1),
        INTEGER_GREATER_THAN_OR_EQUAL(2),
        INTEGER_EQUAL(3),
        INTEGER_NOT_EQUAL(4),
        DATETIME_LESS_THAN_OR_EQUAL(5),
        DATETIME_GREATER_THAN_OR_EQUAL(6),
        BOOLEAN_EQUAL(7),
        BOOLEAN_NOT_EQUAL(8);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    class Builder extends ImmutableApplicationRoleConnectionMetadata.Builder {
        protected Builder() {}
    }
}
