package io.disquark.rest.json.message;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = AllowedMentions.class)
interface AllowedMentionsJson {

    Optional<List<AllowedMentions.Type>> parse();

    Optional<List<Snowflake>> roles();

    Optional<List<Snowflake>> users();

    @JsonProperty("replied_user")
    Optional<Boolean> repliedUser();

    enum Type {
        ROLE_MENTIONS("roles"),
        USER_MENTIONS("users"),
        EVERYONE_MENTIONS("everyone");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}
