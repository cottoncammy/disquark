package io.disquark.rest.json.interaction;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.command.ApplicationCommand;

@ImmutableJson
@JsonDeserialize(as = ApplicationCommandInteractionDataOption.class)
interface ApplicationCommandInteractionDataOptionJson {

    String name();

    ApplicationCommand.Option.Type type();

    Optional<JsonNode> value();

    Optional<List<ApplicationCommandInteractionDataOption>> options();

    Optional<Boolean> focused();
}
