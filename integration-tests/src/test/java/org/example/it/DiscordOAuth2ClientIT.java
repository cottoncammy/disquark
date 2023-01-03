package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import org.example.it.extension.SomeExtension2;
import org.example.rest.oauth2.BearerTokenSource;
import org.example.rest.oauth2.DiscordOAuth2Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.example.it.ConfigHelper.configValue;

@ExtendWith(SomeExtension2.class)
class DiscordOAuth2ClientIT extends AuthenticatedDiscordClientIT {

    @BeforeAll
    static void init() {
        DiscordOAuth2Client.create(BearerTokenSource.create(Vertx.vertx(), configValue("DISCORD_CLIENT_ID", String.class), configValue("DISCORD_CLIENT_SECRET", String.class)).fromClientCredentials());
    }

    @Test
    void testGetGlobalApplicationCommands(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testCreateGlobalApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testGetGlobalApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testEditGlobalApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testDeleteGlobalApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testBulkOverwriteGlobalApplicationCommands(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testGetGuildApplicationCommands(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testCreateGuildApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testGetGuildApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testEditGuildApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testDeleteGuildApplicationCommand(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testBulkOverwriteGuildApplicationCommands(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testGetGuildApplicationCommandPermissions(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testGetApplicationCommandPermissions(DiscordOAuth2Client<?> oAuth2Client) {

    }

    @Test
    void testGetCurrentUser() {
        testGetCurrentUser();
    }

    @Test
    void testGetCurrentUserGuilds() {
        testGetCurrentUserGuilds();
    }

    @Test
    void testGetCurrentUserGuildMember() {
        testGetCurrentUserGuildMember();
    }

    @Test
    void testGetUserConnections(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getUserConnections().subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    void testGetCurrentAuthorizationInformation(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getCurrentAuthorizationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}
