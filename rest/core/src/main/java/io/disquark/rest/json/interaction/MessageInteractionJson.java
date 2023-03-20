package io.disquark.rest.json.interaction;

import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.member.GuildMember;
import io.disquark.rest.json.user.User;

@ImmutableJson
@JsonDeserialize(as = MessageInteraction.class)
interface MessageInteractionJson {

    Snowflake id();

    Interaction.Type type();

    String name();

    User user();

    Optional<GuildMember> member();
}
