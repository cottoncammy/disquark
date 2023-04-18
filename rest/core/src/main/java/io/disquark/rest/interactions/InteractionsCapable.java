package io.disquark.rest.interactions;

import io.disquark.rest.interactions.dsl.InteractionSchema;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.interaction.CreateFollowupMessageUni;
import io.disquark.rest.json.interaction.EditFollowupMessageUni;
import io.disquark.rest.json.interaction.EditOriginalInteractionResponseUni;
import io.disquark.rest.json.message.Message;
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
