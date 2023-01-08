package org.example.rest.interactions.dsl;

import io.vertx.mutiny.core.http.HttpServerResponse;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.interactions.MessageComponentInteraction;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class MessageComponentBuilder implements InteractionSchema<Interaction.MessageComponentData, MessageComponentInteraction> {
    @Nullable
    private String customId;
    @Nullable
    private Component.Type type;

    protected MessageComponentBuilder() {}

    public MessageComponentBuilder customId(String customId) {
        this.customId = requireNonNull(customId, "customId");
        return this;
    }

    public MessageComponentBuilder type(Component.Type type) {
        this.type = requireNonNull(type, "type");
        return this;
    }

    @Override
    public boolean validate(Interaction<Interaction.MessageComponentData> interaction) {
        Optional<Interaction.MessageComponentData> data = interaction.data();
        return interaction.type() == Interaction.Type.MESSAGE_COMPONENT &&
                (customId == null || Objects.equals(customId, data.map(Interaction.MessageComponentData::customId).orElse(null))) &&
                (type == null || Objects.equals(type, data.map(Interaction.MessageComponentData::componentType).orElse(null)));
    }

    @Override
    public MessageComponentInteraction getCompletableInteraction(Interaction<Interaction.MessageComponentData> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
        return new MessageComponentInteraction(interaction, response, interactionsClient);
    }
}
