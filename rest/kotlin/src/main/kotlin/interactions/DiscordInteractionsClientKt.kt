package io.disquark.rest.kotlin.interactions

import io.disquark.rest.interactions.DiscordInteractionsClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.interaction.CreateFollowupMessage
import io.disquark.rest.kotlin.json.interaction.EditFollowupMessage
import io.disquark.rest.kotlin.json.interaction.EditOriginalInteractionResponse
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni

fun <T : Response> DiscordInteractionsClient<T>.editOriginalInteractionResponse(applicationId: Snowflake, interactionToken: String, init: EditOriginalInteractionResponse.() -> Unit): Uni<Message> =
    EditOriginalInteractionResponse(requester, applicationId, interactionToken).apply(init).toUni()

fun <T : Response> DiscordInteractionsClient<T>.createFollowupMessage(applicationId: Snowflake, interactionToken: String, init: CreateFollowupMessage.() -> Unit): Uni<Message> =
    CreateFollowupMessage(requester, applicationId, interactionToken).apply(init).toUni()

fun <T : Response> DiscordInteractionsClient<T>.editFollowupMessage(applicationId: Snowflake, interactionToken: String, messageId: Snowflake, init: EditFollowupMessage.() -> Unit): Uni<Message> =
    EditFollowupMessage(requester, applicationId, interactionToken, messageId).apply(init).toUni()
