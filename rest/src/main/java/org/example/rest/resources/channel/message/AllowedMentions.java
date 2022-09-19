package org.example.rest.resources.channel.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.util.Snowflake;
import org.example.rest.immutables.ImmutableJson;

import java.util.List;

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

        Type(String value) {
            this.value = value;
        }
    }

    class Builder extends ImmutableAllowedMentions.Builder {
        protected Builder() {}
    }
}
