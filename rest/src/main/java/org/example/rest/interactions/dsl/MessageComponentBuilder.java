package org.example.rest.interactions.dsl;

import org.example.rest.interactions.MessageComponentInteraction;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

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
                    Optional<Interaction.MessageComponentData> data = interaction.data();
                    return interaction.type() == Interaction.Type.MESSAGE_COMPONENT &&
                            (customId == null || Objects.equals(customId, data.map(Interaction.MessageComponentData::customId).orElse(null))) &&
                            (type == null || Objects.equals(type, data.map(Interaction.MessageComponentData::componentType).orElse(null)));
                },
                MessageComponentInteraction::new);
    }
}
