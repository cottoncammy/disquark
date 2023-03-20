package io.disquark.rest.interactions;

import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.interaction.CreateFollowupMessageUni;
import io.disquark.rest.json.interaction.EditFollowupMessageUni;
import io.disquark.rest.json.interaction.EditOriginalInteractionResponseUni;
import io.disquark.rest.json.interaction.Interaction;
import io.disquark.rest.json.message.Message;
import io.smallrye.mutiny.Uni;

public class RespondedInteraction<T> {
    private final Interaction<T> interaction;
    private final DiscordInteractionsClient<?> interactionsClient;

    RespondedInteraction(Interaction<T> interaction, DiscordInteractionsClient<?> interactionsClient) {
        this.interaction = interaction;
        this.interactionsClient = interactionsClient;
    }

    public Interaction<T> getInteraction() {
        return interaction;
    }

    public Uni<Message> getOriginalInteractionResponse() {
        return interactionsClient.getOriginalInteractionResponse(interaction.applicationId(), interaction.token());
    }

    public EditOriginalInteractionResponseUni editOriginalInteractionResponse() {
        return interactionsClient.editOriginalInteractionResponse(interaction.applicationId(), interaction.token());
    }

    public Uni<Void> deleteOriginalInteractionResponse() {
        return interactionsClient.deleteOriginalInteractionResponse(interaction.applicationId(), interaction.token());
    }

    public CreateFollowupMessageUni createFollowupMessage() {
        return interactionsClient.createFollowupMessage(interaction.applicationId(), interaction.token());
    }

    public Uni<Message> getFollowupMessage(Snowflake messageId) {
        return interactionsClient.getFollowupMessage(interaction.applicationId(), interaction.token(), messageId);
    }

    public EditFollowupMessageUni editFollowupMessage(Snowflake messageId) {
        return interactionsClient.editFollowupMessage(interaction.applicationId(), interaction.token(), messageId);
    }

    public Uni<Void> deleteFollowupMessage(Snowflake messageId) {
        return interactionsClient.deleteFollowupMessage(interaction.applicationId(), interaction.token(), messageId);
    }
}
