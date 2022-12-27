package org.example.rest.interactions;

import io.smallrye.mutiny.Uni;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.CreateFollowupMessage;
import org.example.rest.resources.interactions.EditFollowupMessage;
import org.example.rest.resources.interactions.EditOriginalInteractionResponse;
import org.example.rest.resources.interactions.Interaction;

public class RespondedInteraction<T> {
    private final Interaction<T> interaction;
    private final DiscordInteractionsClient<?> interactionsClient;

    RespondedInteraction(DiscordInteractionsClient<?> interactionsClient, Interaction<T> interaction) {
        this.interactionsClient = interactionsClient;
        this.interaction = interaction;
    }

    public Interaction<T> getInteraction() {
        return interaction;
    }

    public Uni<Message> getOriginalInteractionResponse() {
        return interactionsClient.getOriginalInteractionResponse(interaction.applicationId(), interaction.token());
    }

    // TODO
    public Uni<Message> editOriginalInteractionResponse(EditOriginalInteractionResponse editOriginalInteractionResponse) {

    }

    public Uni<Void> deleteOriginalInteractionResponse() {
        return interactionsClient.deleteOriginalInteractionResponse(interaction.applicationId(), interaction.token());
    }

    // TODO
    public Uni<Message> createFollowupMessage(CreateFollowupMessage createFollowupMessage) {

    }

    public Uni<Message> getFollowupMessage(Snowflake messageId) {
        return interactionsClient.getFollowupMessage(interaction.applicationId(), interaction.token(), messageId);
    }

    // TODO
    public Uni<Message> editFollowupMessage(EditFollowupMessage editFollowupMessage) {

    }

    public Uni<Void> deleteFollowupMessage(Snowflake messageId) {
        return interactionsClient.deleteFollowupMessage(interaction.applicationId(), interaction.token(), messageId);
    }
}
