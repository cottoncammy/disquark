package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.util.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.permissions.Role;

import java.util.List;
import java.util.Optional;

@ImmutableJson
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
