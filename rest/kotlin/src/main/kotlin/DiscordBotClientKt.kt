package io.disquark.rest.kotlin

import io.disquark.rest.DiscordBotClient
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.message.Message
import io.disquark.rest.kotlin.json.message.CreateMessage
import io.disquark.rest.kotlin.json.message.EditMessage
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni

fun <T : Response> DiscordBotClient<T>.createMessage(channelId: Snowflake, init: CreateMessage.() -> Unit): Uni<Message> =
    CreateMessage(requester, channelId).apply(init).toUni()

fun <T : Response> DiscordBotClient<T>.editMessage(channelId: Snowflake, messageId: Snowflake, init: EditMessage.() -> Unit): Uni<Message> =
    EditMessage(requester, channelId, messageId).apply(init).toUni()
