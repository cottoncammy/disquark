package org.example.rest.resources.interactions;

import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.user.User;

@ImmutableJson
@JsonDeserialize(as = ImmutableMessageInteraction.class)
public interface MessageInteraction {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    Interaction.Type type();

    String name();

    User user();

    Optional<Guild.Member> member();

    class Builder extends ImmutableMessageInteraction.Builder {
        protected Builder() {}
    }
}
