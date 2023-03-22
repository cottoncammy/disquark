package io.disquark.rest.kotlin

import io.disquark.rest.DiscordBotClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessageRequest
import io.disquark.rest.kotlin.json.message.EditMessageRequest
import io.disquark.rest.kotlin.request.`as`
import io.disquark.rest.kotlin.request.requestDeferred
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending

fun <T : Response> DiscordBotClient<T>.createMessage(channelId: Snowflake, init: CreateMessageRequest.() -> Unit): Uni<Message> {
    return requester.requestDeferred(CreateMessageRequest(requester, channelId)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordBotClient<T>.createMessageSuspending(channelId: Snowflake, init: CreateMessageRequest.() -> Unit): Message =
    createMessage(channelId, init).awaitSuspending()

fun <T : Response> DiscordBotClient<T>.editMessage(channelId: Snowflake, messageId: Snowflake, init: EditMessageRequest.() -> Unit): Uni<Message> {
    return requester.requestDeferred(EditMessageRequest(requester, channelId, messageId)
        .apply(init))
        .flatMap { it.`as`() }
}

suspend fun <T : Response> DiscordBotClient<T>.editMessageSuspending(channelId: Snowflake, messageId: Snowflake, init: EditMessageRequest.() -> Unit): Message =
    editMessage(channelId, messageId, init).awaitSuspending()
