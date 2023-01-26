package io.disquark.tutorials;

import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.CreateMessage;
import io.disquark.rest.resources.channel.message.Message;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;

class GettingStarted {

    void createMessage() {
        // <create-message>
        Uni<Message> uni = DiscordBotClient.create(Vertx.vertx(), "BOT_TOKEN")
                .createMessage(CreateMessage.builder().channelId(Snowflake.create(0L)).content("Hello World!").build());
        // </create-message>
    }

    void createMessageAwait() {
        // <create-message-await>
        DiscordBotClient.create(Vertx.vertx(), "BOT_TOKEN")
                .createMessage(CreateMessage.builder().channelId(Snowflake.create(0L)).content("Hello World!").build())
                .await().indefinitely();
        // </create-message-await>
    }
}
