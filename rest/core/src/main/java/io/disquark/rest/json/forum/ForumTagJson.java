package io.disquark.rest.json.forum;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = ForumTag.class)
interface ForumTagJson {

    Snowflake id();

    String name();

    boolean moderated();

    @JsonProperty("emoji_id")
    Snowflake emojiId();

    @JsonProperty("emoji_name")
    Optional<String> emojiName();
}
