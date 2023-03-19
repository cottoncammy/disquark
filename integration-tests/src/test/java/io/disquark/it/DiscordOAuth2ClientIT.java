package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.json.command.ApplicationCommandPermissions;
import io.disquark.rest.oauth2.DiscordOAuth2Client;
import io.disquark.rest.json.Snowflake;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class DiscordOAuth2ClientIT {

    @Test
    void testEditApplicationCommandPermissions(DiscordOAuth2Client<?> oAuth2Client,
            @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        Snowflake applicationId = oAuth2Client.getCurrentAuthorizationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .application()
                .id();

        oAuth2Client.createGuildApplicationCommand(applicationId, guildId, "foo")
                .withDescription("bar")
                .call(command -> oAuth2Client.editApplicationCommandPermissions(applicationId, guildId, command.id())
                        .withPermissions(new ApplicationCommandPermissions(guildId,
                                ApplicationCommandPermissions.Type.ROLE, true)))
                .onItemOrFailure().call((command, e) -> oAuth2Client.deleteGuildApplicationCommand(applicationId,
                        guildId, command.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    void testGetCurrentUserGuildMember(DiscordOAuth2Client<?> oAuth2Client,
            @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        oAuth2Client.getCurrentUserGuildMember(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    void testGetUserConnections(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getUserConnections()
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    void testGetCurrentAuthorizationInformation(DiscordOAuth2Client<?> oAuth2Client) {
        oAuth2Client.getCurrentAuthorizationInformation()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
