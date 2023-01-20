package org.example.rest.resources.channel.forum;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;

@ImmutableJson
@JsonDeserialize(as = ImmutableDefaultReaction.class)
public interface DefaultReaction {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("emoji_id")
    Optional<Snowflake> emojiId();

    @JsonProperty("emoji_name")
    Optional<String> emojiName();

    class Builder extends ImmutableDefaultReaction.Builder {
        protected Builder() {}
    }
}
