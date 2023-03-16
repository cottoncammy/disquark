package io.disquark.rest.resources.emoji;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.permissions.Role;
import io.disquark.rest.resources.user.User;

@ImmutableJson
@JsonDeserialize(as = Emoji.class)
interface EmojiJson {

    Optional<Snowflake> id();

    Optional<String> name();

    Optional<List<Role>> roles();

    Optional<User> user();

    @JsonProperty("require_colons")
    Optional<Boolean> requireColons();

    Optional<Boolean> managed();

    Optional<Boolean> animated();

    Optional<Boolean> available();
}
