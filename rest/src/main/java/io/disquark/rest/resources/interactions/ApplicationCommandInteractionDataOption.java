package io.disquark.rest.resources.interactions;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.application.command.ApplicationCommand;

@ImmutableJson
@JsonDeserialize(as = ImmutableApplicationCommandInteractionDataOption.class)
public interface ApplicationCommandInteractionDataOption {

    static Builder builder() {
        return new Builder();
    }

    String name();

    ApplicationCommand.Option.Type type();

    Optional<JsonNode> value();

    Optional<List<ApplicationCommandInteractionDataOption>> options();

    Optional<Boolean> focused();

    class Builder extends ImmutableApplicationCommandInteractionDataOption.Builder {
        protected Builder() {
        }
    }
}
