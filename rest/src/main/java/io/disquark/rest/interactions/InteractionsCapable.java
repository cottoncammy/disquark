package io.disquark.rest.interactions;

import io.disquark.rest.interactions.dsl.InteractionSchema;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.CreateFollowupMessageUni;
import io.disquark.rest.resources.interactions.EditFollowupMessageUni;
import io.disquark.rest.resources.interactions.EditOriginalInteractionResponseUni;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface InteractionsCapable {

    <D, C extends CompletableInteraction<D>> Multi<C> on(InteractionSchema<D, C> schema);

    Uni<Message> getOriginalInteractionResponse(Snowflake applicationId, String interactionToken);

    EditOriginalInteractionResponseUni editOriginalInteractionResponse(Snowflake applicationId, String interactionToken);

    Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken);

    CreateFollowupMessageUni createFollowupMessage(Snowflake applicationId, String interactionToken);

    Uni<Message> getFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId);

    EditFollowupMessageUni editFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId);

    Uni<Void> deleteFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId);
}
