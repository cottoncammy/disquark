package org.example.rest.resources.channel.forum;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.ImmutableDefaultReaction;

import java.util.Optional;

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
