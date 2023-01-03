package org.example.it;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.extension.SomeExtension;
import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.user.GetCurrentUserGuilds;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

class AuthenticatedDiscordClientIT {

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUser(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUser().subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUserGuilds(AuthenticatedDiscordClient<?> discordClient) {
        discordClient.getCurrentUserGuilds(GetCurrentUserGuilds.create()).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @TestTemplate
    @ExtendWith(SomeExtension.class)
    void testGetCurrentUserGuildMember(AuthenticatedDiscordClient<?> discordClient, Snowflake guildId) {
        discordClient.getCurrentUserGuildMember(guildId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }
}
