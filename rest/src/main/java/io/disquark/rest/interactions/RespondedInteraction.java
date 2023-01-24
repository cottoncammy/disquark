package io.disquark.rest.interactions;

import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.CreateFollowupMessage;
import io.disquark.rest.resources.interactions.EditFollowupMessage;
import io.disquark.rest.resources.interactions.EditOriginalInteractionResponse;
import io.disquark.rest.resources.interactions.Interaction;
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

    // TODO
    public Uni<Message> editOriginalInteractionResponse(EditOriginalInteractionResponse editOriginalInteractionResponse) {
        return interactionsClient.editOriginalInteractionResponse(null);
    }

    public Uni<Void> deleteOriginalInteractionResponse() {
        return interactionsClient.deleteOriginalInteractionResponse(interaction.applicationId(), interaction.token());
    }

    // TODO & do requireNonNull
    public Uni<Message> createFollowupMessage(CreateFollowupMessage createFollowupMessage) {
        return interactionsClient.createFollowupMessage(null);
    }

    public Uni<Message> getFollowupMessage(Snowflake messageId) {
        return interactionsClient.getFollowupMessage(interaction.applicationId(), interaction.token(), messageId);
    }

    // TODO
    public Uni<Message> editFollowupMessage(EditFollowupMessage editFollowupMessage) {
        return interactionsClient.editFollowupMessage(null);
    }

    public Uni<Void> deleteFollowupMessage(Snowflake messageId) {
        return interactionsClient.deleteFollowupMessage(interaction.applicationId(), interaction.token(), messageId);
    }
}
