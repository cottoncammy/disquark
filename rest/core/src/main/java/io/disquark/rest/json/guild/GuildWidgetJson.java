package io.disquark.rest.json.guild;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = GuildWidget.class)
interface GuildWidgetJson {

    Snowflake id();

    String name();

    @JsonProperty("instant_invite")
    Optional<String> instantInvite();

    List<GuildWidget.PartialChannel> channels();

    List<GuildWidget.UserPresence> members();

    @JsonProperty("presence_count")
    int presenceCount();

    @ImmutableJson
    @JsonDeserialize(as = GuildWidget.PartialChannel.class)
    interface PartialChannelJson {

        Snowflake id();

        OptionalInt position();

        Optional<String> name();
    }

    @ImmutableJson
    @JsonDeserialize(as = GuildWidget.UserPresence.class)
    interface UserPresenceJson {

        Snowflake id();

        String username();

        String discriminator();

        Optional<String> avatar();

        String status();

        String avatarUrl();
    }
}
