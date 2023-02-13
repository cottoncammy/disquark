package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.interactions.MessageComponentInteraction;
import io.disquark.rest.resources.interactions.Interaction;
import io.disquark.rest.resources.interactions.components.Component;
import io.vertx.mutiny.ext.web.RoutingContext;

public class MessageComponentBuilder
        implements InteractionSchema<Interaction.MessageComponentData, MessageComponentInteraction> {
    @Nullable
    private String customId;
    @Nullable
    private Component.Type type;

    protected MessageComponentBuilder() {
    }

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
                (customId == null
                        || Objects.equals(customId, data.map(Interaction.MessageComponentData::customId).orElse(null)))
                &&
                (type == null || Objects.equals(type, data.map(Interaction.MessageComponentData::componentType).orElse(null)));
    }

    @Override
    public MessageComponentInteraction getCompletableInteraction(RoutingContext context,
            Interaction<Interaction.MessageComponentData> interaction,
            DiscordInteractionsClient<?> interactionsClient) {
        return new MessageComponentInteraction(context, interaction, interactionsClient);
    }
}
