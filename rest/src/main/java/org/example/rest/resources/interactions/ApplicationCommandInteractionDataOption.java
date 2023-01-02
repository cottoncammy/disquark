package org.example.rest.resources.interactions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.application.command.ApplicationCommand;

import java.util.List;
import java.util.Optional;

@ImmutableJson
@JsonDeserialize(as = ImmutableApplicationCommandInteractionDataOption.class)
public interface ApplicationCommandInteractionDataOption {

    static Builder builder() {
        return new Builder();
    }

    String name();

    ApplicationCommand.Option.Type type();

    // TODO
    Optional<Object> value();

    Optional<List<ApplicationCommandInteractionDataOption>> options();

    Optional<Boolean> focused();

    class Builder extends ImmutableApplicationCommandInteractionDataOption.Builder {
        protected Builder() {}
    }
}
