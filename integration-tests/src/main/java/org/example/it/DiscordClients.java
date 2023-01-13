package org.example.it;

import io.vertx.mutiny.core.Vertx;
import org.example.it.config.ConfigHelper;
import org.example.rest.DiscordBotClient;
import org.example.rest.oauth2.BearerTokenSource;
import org.example.rest.oauth2.DiscordOAuth2Client;
import org.example.rest.request.Requester;
import org.example.rest.response.Response;

public class DiscordClients {

    public static Vertx getVertx() {
        return LazyHolder.VERTX;
    }

    public static DiscordBotClient<?> getBotClient() {
        return LazyHolder.BOT_CLIENT;
    }

    public static DiscordOAuth2Client<?> getOAuth2Client() {
        return LazyHolder.OAUTH2_CLIENT;
    }

    private static class LazyHolder {
        static final Vertx VERTX = Vertx.vertx();
        static final DiscordBotClient<?> BOT_CLIENT;
        static final DiscordOAuth2Client<?> OAUTH2_CLIENT;

        static {
            BOT_CLIENT = DiscordBotClient.create(VERTX, ConfigHelper.configValue("DISCORD_TOKEN", String.class));
            BearerTokenSource.Builder builder = BearerTokenSource.create(
                    VERTX,
                    ConfigHelper.configValue("DISCORD_CLIENT_ID", String.class),
                    ConfigHelper.configValue("DISCORD_CLIENT_SECRET", String.class));
            OAUTH2_CLIENT = DiscordOAuth2Client.create(builder.fromClientCredentials());
        }

    }

    private DiscordClients() {}
}
