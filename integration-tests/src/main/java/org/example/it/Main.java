package org.example.it;

import io.vertx.mutiny.core.Vertx;
import org.example.rest.DiscordBotClient;
import org.example.rest.request.channel.message.CreateMessage;
import org.example.rest.resources.Snowflake;
import org.example.rest.response.HttpResponse;

import java.util.Arrays;

import static org.example.it.ConfigHelper.configValue;

// TODO remove
public class Main {

    public static void main(String[] args) {
        DiscordBotClient<HttpResponse> botClient = DiscordBotClient.create(Vertx.vertx(), configValue("DISCORD_TOKEN", String.class));
        CreateMessage createMessage = CreateMessage.builder().channelId(configValue("DISCORD_CHANNEL_ID", Snowflake.class)).content("Hello World!").build();
        botClient.createMessage(createMessage).subscribe().with(x -> System.out.println("Completed"), f -> {
            System.out.println("Failure: " + f.getMessage());
            System.out.println("Stacktrace: " + Arrays.toString(f.getStackTrace()));
        });
    }
}
