package org.example.rest.resources.channel.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;

import java.util.List;

import static java.util.Objects.requireNonNull;

@ImmutableJson
public interface AllowedMentions {

    static Builder builder() {
        return new Builder();
    }

    List<Type> parse();

    List<Snowflake> roles();

    List<Snowflake> users();

    @JsonProperty("replied_user")
    boolean repliedUser();

    enum Type {
        ROLE_MENTIONS("roles"),
        USER_MENTIONS("users"),
        EVERYONE_MENTIONS("everyone");

        private final String value;

        public static Type create(String value) {
            switch (requireNonNull(value)) {
                case "roles":
                    return ROLE_MENTIONS;
                case "users":
                    return USER_MENTIONS;
                case "everyone":
                    return EVERYONE_MENTIONS;
                default:
                    throw new IllegalArgumentException();
            }
        }

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    class Builder extends ImmutableAllowedMentions.Builder {
        protected Builder() {}
    }
}
