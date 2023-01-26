package io.disquark.rest.resources.channel.forum;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

@ImmutableJson
@JsonDeserialize(as = ImmutableForumTag.class)
public interface ForumTag {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    String name();

    boolean moderated();

    @JsonProperty("emoji_id")
    Snowflake emojiId();

    @JsonProperty("emoji_name")
    Optional<String> emojiName();

    class Builder extends ImmutableForumTag.Builder {
        protected Builder() {
        }
    }
}