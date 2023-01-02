package org.example.rest.interactions.dsl;

import org.example.rest.interactions.ModalSubmitInteraction;
import org.example.rest.resources.interactions.Interaction;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

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
                    Optional<Interaction.ModalSubmitData> data = interaction.data();
                    return interaction.type() == Interaction.Type.MODAL_SUBMIT &&
                            (customId == null || Objects.equals(customId, data.map(Interaction.ModalSubmitData::customId).orElse(null)));
                },
                ModalSubmitInteraction::new);
    }
}
