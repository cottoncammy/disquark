package org.example.rest.interactions.schema.dsl;

import org.example.rest.interactions.ModalSubmitInteraction;
import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;

import javax.annotation.Nullable;
import java.util.Objects;

public class ModalSubmitBuilder implements Buildable<Interaction.ModalSubmitData, ModalSubmitInteraction> {
    @Nullable
    private String customId;

    protected ModalSubmitBuilder() {}

    public ModalSubmitBuilder customId(String customId) {
        this.customId = customId;
        return this;
    }

    @Override
    public InteractionSchema<Interaction.ModalSubmitData, ModalSubmitInteraction> schema() {
        return new InteractionSchema<>(
                interaction -> {
                    return interaction.type() == Interaction.Type.MODAL_SUBMIT &&
                            interaction.data().isPresent() &&
                            Objects.equals(interaction.data().get().customId(), customId);

                },
                ModalSubmitInteraction::new
        );
    }
}
