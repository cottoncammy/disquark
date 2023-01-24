package io.disquark.rest.interactions.dsl;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import javax.annotation.Nullable;

import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.interactions.ModalSubmitInteraction;
import io.disquark.rest.resources.interactions.Interaction;
import io.vertx.mutiny.core.http.HttpServerResponse;

public class ModalSubmitBuilder implements InteractionSchema<Interaction.ModalSubmitData, ModalSubmitInteraction> {
    @Nullable
    private String customId;

    protected ModalSubmitBuilder() {
    }

    public ModalSubmitBuilder customId(String customId) {
        this.customId = requireNonNull(customId, "customId");
        return this;
    }

    @Override
    public boolean validate(Interaction<Interaction.ModalSubmitData> interaction) {
        return interaction.type() == Interaction.Type.MODAL_SUBMIT &&
                (customId == null || Objects.equals(customId,
                        interaction.data().map(Interaction.ModalSubmitData::customId).orElse(null)));
    }

    @Override
    public ModalSubmitInteraction getCompletableInteraction(Interaction<Interaction.ModalSubmitData> interaction,
            HttpServerResponse response, DiscordInteractionsClient<?> interactionsClient) {
        return new ModalSubmitInteraction(interaction, response, interactionsClient);
    }
}
