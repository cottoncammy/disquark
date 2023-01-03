package org.example.it;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.extension.ConfigValue;
import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.CreateGuild;
import org.example.rest.resources.user.ModifyCurrentUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SomeExtension2.class)
class UserIT extends AuthenticatedDiscordClientIT {

    @Test
    void testGetCurrentUser(DiscordBotClient<?> botClient) {
        super.testGetCurrentUser(botClient);
    }

    @Test
    void testGetUser(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.getUser(userId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    void testModifyCurrentUser(DiscordBotClient<?> botClient) {
        ModifyCurrentUser.Builder builder = ModifyCurrentUser.builder().avatar("");
        botClient.getCurrentUser()
                .call(user -> botClient.modifyCurrentUser(builder.build()))
                .flatMap(user -> botClient.modifyCurrentUser(builder.avatar("").build()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    void testGetCurrentUserGuilds(DiscordBotClient<?> botClient) {
        super.testGetCurrentUserGuilds(botClient);
    }

    @Test
    void testGetCurrentUserGuildMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        super.testGetCurrentUserGuildMember(botClient, guildId);
    }

    @Test
    void testLeaveGuild(DiscordBotClient<?> botClient) {
        botClient.createGuild(CreateGuild.builder().name("foo").build())
                .flatMap(guild -> botClient.leaveGuild(guild.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    void testCreateDm(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.createDm(userId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }
}
