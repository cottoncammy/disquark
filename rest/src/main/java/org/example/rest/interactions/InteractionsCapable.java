package org.example.rest.interactions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.example.rest.interactions.schema.InteractionSchema;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.CreateFollowupMessage;
import org.example.rest.resources.interactions.EditFollowupMessage;
import org.example.rest.resources.interactions.EditOriginalInteractionResponse;

public interface InteractionsCapable {

    <D, C extends CompletableInteraction<D>> Multi<C> on(InteractionSchema<D, C> schema);

    Uni<Message> getOriginalInteractionResponse(Snowflake applicationId, String interactionToken);

    Uni<Message> editOriginalInteractionResponse(EditOriginalInteractionResponse editOriginalInteractionResponse);

    Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken);

    Uni<Message> createFollowupMessage(CreateFollowupMessage createFollowupMessage);

    Uni<Message> getFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId);

    Uni<Message> editFollowupMessage(EditFollowupMessage editFollowupMessage);

    Uni<Void> deleteFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId);
}
