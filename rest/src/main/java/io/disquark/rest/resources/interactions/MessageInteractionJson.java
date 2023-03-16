package io.disquark.rest.resources.interactions;

import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.guild.Guild;
import io.disquark.rest.resources.user.User;

@ImmutableJson
@JsonDeserialize(as = MessageInteraction.class)
interface MessageInteractionJson {

    Snowflake id();

    Interaction.Type type();

    String name();

    User user();

    Optional<Guild.Member> member();
}
