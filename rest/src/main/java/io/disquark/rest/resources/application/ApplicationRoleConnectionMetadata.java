package io.disquark.rest.resources.application;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Locale;

@ImmutableJson
@JsonDeserialize(as = ImmutableApplicationRoleConnectionMetadata.class)
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
        @JsonEnumDefaultValue
        UNKNOWN(-1),
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
        protected Builder() {
        }
    }
}