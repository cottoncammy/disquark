package org.example.rest.resources.emoji;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.user.User;
import org.example.rest.resources.permissions.Role;

import java.util.List;
import java.util.Optional;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableEmoji.class)
public interface Emoji {

    static Builder builder() {
        return new Builder();
    }

    Optional<Snowflake> id();

    Optional<String> name();

    Optional<List<Role>> roles();

    Optional<User> user();

    @JsonProperty("require_colons")
    Optional<Boolean> requireColons();

    Optional<Boolean> managed();

    Optional<Boolean> animated();

    Optional<Boolean> available();

    class Builder extends ImmutableEmoji.Builder {
        protected Builder() {}
    }
}
