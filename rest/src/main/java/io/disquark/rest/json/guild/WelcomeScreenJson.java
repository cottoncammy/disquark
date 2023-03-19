package io.disquark.rest.json.guild;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = WelcomeScreen.class)
interface WelcomeScreenJson {

    Optional<String> description();

    @JsonProperty("welcome_channels")
    List<WelcomeScreen.Channel> welcomeChannels();

    @ImmutableJson
    @JsonDeserialize(as = WelcomeScreen.Channel.class)
    interface ChannelJson {

        @JsonProperty("channel_id")
        Snowflake channelId();

        String description();

        @JsonProperty("emoji_id")
        Optional<Snowflake> emojiId();

        @JsonProperty("emoji_name")
        Optional<String> emojiName();
    }
}
