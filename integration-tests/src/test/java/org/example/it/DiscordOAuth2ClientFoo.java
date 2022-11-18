package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.oauth2.BearerTokenSource;
import org.example.rest.oauth2.DiscordOAuth2Client;
import org.example.rest.response.HttpResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.example.it.ConfigHelper.configValue;

class DiscordOAuth2ClientFoo {
    private static DiscordOAuth2Client<HttpResponse> oAuth2Client;

    @BeforeAll
    static void init() {
        oAuth2Client = DiscordOAuth2Client.create(BearerTokenSource.create(Vertx.vertx(), configValue("DISCORD_CLIENT_ID", String.class), configValue("DISCORD_CLIENT_SECRET", String.class)).fromClientCredentials());
    }

    @Test
    void testGetUserConnections() {
        oAuth2Client.getUserConnections().subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }
}
