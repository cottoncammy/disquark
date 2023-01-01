package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.MessageComponentInteraction;
import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import javax.annotation.Nullable;
import java.util.Objects;

public class MessageComponentBuilder implements Buildable<Interaction.MessageComponentData, MessageComponentInteraction> {
    @Nullable
    private String customId;
    @Nullable
    private Component.Type type;

    protected MessageComponentBuilder() {}

    public MessageComponentBuilder customId(String customId) {
        this.customId = customId;
        return this;
    }

    public MessageComponentBuilder type(Component.Type type) {
        this.type = type;
        return this;
    }

    @Override
    public InteractionSchema<Interaction.MessageComponentData, MessageComponentInteraction> schema() {
        return new InteractionSchema<>(
                interaction -> {
                    return interaction.type() == Interaction.Type.MESSAGE_COMPONENT &&
                            interaction.data().isPresent() &&
                            Objects.equals(interaction.data().get().customId(), customId) &&
                            Objects.equals(interaction.data().get().componentType(), type);
                },
                MessageComponentInteraction::new
        );
    }
}
