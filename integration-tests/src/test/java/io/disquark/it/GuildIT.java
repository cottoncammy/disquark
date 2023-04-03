package io.disquark.it;

import java.util.EnumSet;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.channel.GuildChannelPosition;
import io.disquark.rest.json.guild.Guild;
import io.disquark.rest.json.oauth2.AccessToken;
import io.disquark.rest.json.oauth2.Scope;
import io.disquark.rest.json.role.GuildRolePosition;
import io.disquark.rest.oauth2.BearerTokenSource;
import io.disquark.rest.response.DiscordException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class GuildIT {
    private Snowflake guildId;
    private Snowflake roleId;

    @BeforeAll
    void init(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        botClient.getCurrentUserGuilds()
                .filter(guild -> !guild.id().equals(guildId))
                .onItem().transformToUniAndMerge(guild -> botClient.deleteGuild(guild.id())
                        .onFailure(DiscordException.statusCodeIs(403)).recoverWithNull())
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem();
    }

    @Test
    @Order(1)
    void testCreateGuild(DiscordBotClient<?> botClient) {
        guildId = botClient.createGuild("foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(2)
    void testGetGuild(DiscordBotClient<?> botClient) {
        botClient.getGuild(guildId, false)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(3)
    void testGetGuildPreview(DiscordBotClient<?> botClient) {
        botClient.getGuildPreview(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testModifyGuild(DiscordBotClient<?> botClient) {
        botClient.modifyGuild(guildId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testModifyGuildChannelPositions(DiscordBotClient<?> botClient) {
        botClient.getGuildChannels(guildId)
                .toUni()
                .call(channel -> botClient.modifyGuildChannelPositions(guildId)
                        .withGuildChannelPositions(new GuildChannelPosition(channel.id()).withPosition(1)))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(6)
    void testListActiveGuildThreads(DiscordBotClient<?> botClient) {
        botClient.listActiveGuildThreads(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("members-intent")
    @Order(7)
    void testListGuildMembers(DiscordBotClient<?> botClient) {
        botClient.listGuildMembers(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(8)
    void testSearchGuildMembers(DiscordBotClient<?> botClient) {
        botClient.searchGuildMembers(guildId, "foo")
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(9)
    void testAddGuildMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CLIENT_ID") String clientId,
            @ConfigValue("DISCORD_CLIENT_SECRET") String clientSecret, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        Uni<String> tokenUni = BearerTokenSource.create(botClient.getVertx(), clientId, clientSecret)
                .fromClientCredentials(EnumSet.of(Scope.GUILDS_JOIN))
                .getToken()
                .map(AccessToken::accessToken);

        tokenUni.call(token -> botClient.addGuildMember(guildId, userId, token))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(10)
    void testGetGuildMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.getGuildMember(guildId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(11)
    void testModifyGuildMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.modifyGuildMember(guildId, userId)
                .withNick("foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(12)
    void testModifyCurrentMember(DiscordBotClient<?> botClient) {
        botClient.modifyCurrentMember(guildId)
                .withNick("foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(13)
    void testGetGuildBans(DiscordBotClient<?> botClient) {
        botClient.getGuildBans(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(14)
    void testGetGuildRoles(DiscordBotClient<?> botClient) {
        botClient.getGuildRoles(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(15)
    void testCreateGuildRole(DiscordBotClient<?> botClient) {
        roleId = botClient.createGuildRole(guildId)
                .withName("foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(16)
    void testAddGuildMemberRole(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.addGuildMemberRole(guildId, userId, roleId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(17)
    void testRemoveGuildMemberRole(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.removeGuildMemberRole(guildId, userId, roleId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(18)
    void testRemoveGuildMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.removeGuildMember(guildId, userId, null).subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(19)
    void testCreateGuildBan(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.createGuildBan(guildId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(20)
    void testGetGuildBan(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.getGuildBan(guildId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(21)
    void testRemoveGuildBan(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.removeGuildBan(guildId, userId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(22)
    void testModifyGuildRolePositions(DiscordBotClient<?> botClient) {
        botClient.modifyGuildRolePositions(guildId)
                .withGuildRolePositions(new GuildRolePosition(roleId).withPosition(1))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(23)
    void testModifyGuildRole(DiscordBotClient<?> botClient) {
        botClient.modifyGuildRole(guildId, roleId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(24)
    void testModifyGuildMfaLevel(DiscordBotClient<?> botClient) {
        botClient.modifyGuildMfaLevel(guildId, Guild.MfaLevel.NONE)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(25)
    void testDeleteGuildRole(DiscordBotClient<?> botClient) {
        botClient.deleteGuildRole(guildId, roleId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(26)
    void testGetGuildPruneCount(DiscordBotClient<?> botClient) {
        botClient.getGuildPruneCount(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(27)
    void testBeginGuildPrune(DiscordBotClient<?> botClient) {
        botClient.beginGuildPrune(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(28)
    void testGetGuildVoiceRegions(DiscordBotClient<?> botClient) {
        botClient.getGuildVoiceRegions(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(29)
    void testGetGuildInvites(DiscordBotClient<?> botClient) {
        botClient.getGuildInvites(guildId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(30)
    void testDeleteGuildIntegration(DiscordBotClient<?> botClient) {
        botClient.getGuildIntegrations(guildId).toUni()
                .flatMap(integration -> botClient.deleteGuildIntegration(guildId, integration.id(), null))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(31)
    void testGetGuildWidgetSettings(DiscordBotClient<?> botClient) {
        botClient.getGuildWidgetSettings(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(32)
    void testModifyGuildWidget(DiscordBotClient<?> botClient) {
        botClient.modifyGuildWidget(guildId)
                .withEnabled(true)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(33)
    void testGetGuildWidget(DiscordBotClient<?> botClient) {
        botClient.getGuildWidget(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("vanity-url")
    @Order(34)
    void testGetGuildVanityUrl(DiscordBotClient<?> botClient) {
        botClient.getGuildVanityUrl(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("welcome-screen")
    @Order(35)
    void testGetGuildWelcomeScreen(DiscordBotClient<?> botClient) {
        botClient.getGuildWelcomeScreen(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("welcome-screen")
    @Order(36)
    void testModifyGuildWelcomeScreen(DiscordBotClient<?> botClient) {
        botClient.modifyGuildWelcomeScreen(guildId)
                .withDescription("foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("stage-channel")
    @Order(37)
    void testModifyCurrentUserVoiceState(DiscordBotClient<?> botClient) {
        botClient.modifyCurrentUserVoiceState(guildId)
                .withSuppress(true)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("stage-channel")
    @Order(38)
    void testModifyUserVoiceState(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId,
            @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.modifyUserVoiceState(guildId, userId, channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(39)
    void testDeleteGuild(DiscordBotClient<?> botClient) {
        botClient.deleteGuild(guildId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
