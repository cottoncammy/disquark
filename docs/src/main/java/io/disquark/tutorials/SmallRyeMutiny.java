package io.disquark.tutorials;

import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.CreateMessage;
import io.vertx.mutiny.core.Vertx;

public class SmallRyeMutiny {

    void log(Throwable t) {
    }

    void createMessageSubscribe() {
        // <create-message-subscribe>
        DiscordBotClient.create(Vertx.vertx(), "BOT_TOKEN")
                .createMessage(CreateMessage.builder().channelId(Snowflake.create(0L)).content("Hello World!").build())
                .subscribe()
                .with(message -> System.out.println("Sent a message!"));
        // </create-message-subscribe>
    }

    void createMessageOps() {
        DiscordBotClient<?> botClient = null;
        Snowflake channelId = null;

        // <create-message-events>
        botClient.createMessage(CreateMessage.builder().channelId(channelId).content("Hello World!").build())
                .map(message -> message.id()) // we map when we need to transform our item synchronously
                .flatMap(messageId -> botClient.deleteMessage(channelId, messageId, null)) // we flatMap when we need to transform our item asynchronously
                .onFailure().invoke(failure -> log(failure)) // log failures
                .onFailure().recoverWithNull() // discard failures by recovering with a null item
                .subscribe()
                .with(x -> {});
        // </create-message-events>
    }
}
