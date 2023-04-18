package io.disquark.rest.json.guild;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.emoji.Emoji;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = GuildOnboarding.class)
interface GuildOnboardingJson {

    @JsonProperty("guild_id")
    Snowflake guildId();

    List<GuildOnboarding.Prompt> prompts();

    @JsonProperty("default_channel_ids")
    List<Snowflake> defaultChannelIds();

    boolean enabled();

    @ImmutableJson
    @JsonDeserialize(as = GuildOnboarding.Prompt.class)
    interface Prompt {

        Snowflake id();

        GuildOnboarding.Prompt.Type type();

        List<GuildOnboarding.PromptOption> options();

        String title();

        @JsonProperty("single_selects")
        boolean singleSelect();

        boolean required();

        @JsonProperty("in_onboarding")
        boolean inOnboarding();

        enum Type {
            @JsonEnumDefaultValue
            UNKNOWN(-1),
            MULTIPLE_CHOICE(0),
            DROPDOWN(1);

            private final int value;

            Type(int value) {
                this.value = value;
            }

            @JsonValue
            public int getValue() {
                return value;
            }
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = GuildOnboarding.PromptOption.class)
    interface PromptOption {

        Snowflake id();

        @JsonProperty("channel_ids")
        List<Snowflake> channelIds();

        @JsonProperty("role_ids")
        List<Snowflake> roleIds();

        Emoji emoji();

        String title();

        Optional<String> description();
    }
}
