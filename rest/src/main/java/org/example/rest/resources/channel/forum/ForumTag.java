package org.example.rest.resources.channel.forum;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;

import java.util.Optional;

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
        protected Builder() {}
    }
}
