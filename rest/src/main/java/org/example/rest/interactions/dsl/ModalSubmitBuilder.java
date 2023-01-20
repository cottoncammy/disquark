package org.example.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javax.annotation.Nullable;

import io.vertx.mutiny.core.http.HttpServerResponse;

import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.interactions.ModalSubmitInteraction;
import org.example.rest.resources.interactions.Interaction;

public class ModalSubmitBuilder implements InteractionSchema<Interaction.ModalSubmitData, ModalSubmitInteraction> {
    @Nullable
    private String customId;

    protected ModalSubmitBuilder() {}

    public ModalSubmitBuilder customId(String customId) {
        this.customId = requireNonNull(customId, "customId");
        return this;
    }

    @Override
    public boolean validate(Interaction<Interaction.ModalSubmitData> interaction) {
        return interaction.type() == Interaction.Type.MODAL_SUBMIT &&
                (customId == null || Objects.equals(customId, interaction.data().map(Interaction.ModalSubmitData::customId).orElse(null)));
    }

    @Override
    public ModalSubmitInteraction getCompletableInteraction(Interaction<Interaction.ModalSubmitData> interaction, HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
        return new ModalSubmitInteraction(interaction, response, interactionsClient);
    }
}
