package org.example.it.extension;

import io.vertx.mutiny.core.Vertx;
import org.example.rest.DiscordBotClient;
import org.example.rest.oauth2.BearerTokenSource;
import org.example.rest.oauth2.DiscordOAuth2Client;
import org.example.rest.request.Requester;
import org.example.rest.response.Response;

import static org.example.it.ConfigHelper.configValue;

public class DiscordClients {

    public static DiscordBotClient<?> getBotClient() {
        return LazyHolder.BOT_CLIENT;
    }

    public static DiscordOAuth2Client<?> getOAuth2Client() {
        return LazyHolder.OAUTH2_CLIENT;
    }

    @SuppressWarnings("unchecked")
    private static class LazyHolder {
        private static final Vertx VERTX = Vertx.vertx();

        static final DiscordBotClient<?> BOT_CLIENT;
        static final DiscordOAuth2Client<?> OAUTH2_CLIENT;

        static {
            BOT_CLIENT = DiscordBotClient.create(VERTX, configValue("DISCORD_TOKEN", String.class));
            BearerTokenSource.Builder builder = BearerTokenSource.create(
                    VERTX,
                    configValue("DISCORD_CLIENT_ID", String.class),
                    configValue("DISCORD_CLIENT_SECRET", String.class)
            );
            OAUTH2_CLIENT = DiscordOAuth2Client.builder(builder.fromClientCredentials())
                    .requesterFactory(x -> (Requester<Response>) BOT_CLIENT.getRequester())
                    .build();
        }

    }

    private DiscordClients() {}
}
