package io.disquark.rest.interactions;

import io.disquark.rest.interactions.dsl.InteractionSchema;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.CreateFollowupMessage;
import io.disquark.rest.resources.interactions.EditFollowupMessage;
import io.disquark.rest.resources.interactions.EditOriginalInteractionResponse;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

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
