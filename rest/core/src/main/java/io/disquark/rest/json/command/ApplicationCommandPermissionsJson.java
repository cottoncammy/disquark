package io.disquark.rest.json.command;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = ApplicationCommandPermissions.class)
interface ApplicationCommandPermissionsJson {

    Snowflake id();

    ApplicationCommandPermissions.Type type();

    boolean permission();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        ROLE(1),
        USER(2),
        CHANNEL(3);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }
}
