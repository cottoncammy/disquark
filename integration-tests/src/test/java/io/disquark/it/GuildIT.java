package io.disquark.it;

import java.util.EnumSet;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.oauth2.BearerTokenSource;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.resources.guild.AddGuildMember;
import io.disquark.rest.resources.guild.CreateGuild;
import io.disquark.rest.resources.guild.CreateGuildBan;
import io.disquark.rest.resources.guild.CreateGuildRole;
import io.disquark.rest.resources.guild.GetGuildBans;
import io.disquark.rest.resources.guild.Guild;
import io.disquark.rest.resources.guild.ListGuildMembers;
import io.disquark.rest.resources.guild.ModifyCurrentMember;
import io.disquark.rest.resources.guild.ModifyCurrentUserVoiceState;
import io.disquark.rest.resources.guild.ModifyGuild;
import io.disquark.rest.resources.guild.ModifyGuildChannelPositions;
import io.disquark.rest.resources.guild.ModifyGuildMember;
import io.disquark.rest.resources.guild.ModifyGuildRole;
import io.disquark.rest.resources.guild.ModifyGuildRolePositions;
import io.disquark.rest.resources.guild.ModifyGuildWelcomeScreen;
import io.disquark.rest.resources.guild.ModifyGuildWidget;
import io.disquark.rest.resources.guild.SearchGuildMembers;
import io.disquark.rest.resources.guild.prune.BeginGuildPrune;
import io.disquark.rest.resources.guild.prune.GetGuildPruneCount;
import io.disquark.rest.resources.oauth2.AccessToken;
import io.disquark.rest.json.oauth2.Scope;
import io.disquark.rest.resources.user.GetCurrentUserGuilds;
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
        botClient.getCurrentUserGuilds(GetCurrentUserGuilds.create())
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
        guildId = botClient.createGuild(CreateGuild.builder().name("foo").build())
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
        botClient.modifyGuild(ModifyGuild.builder().guildId(guildId).name("bar").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testModifyGuildChannelPositions(DiscordBotClient<?> botClient) {
        ModifyGuildChannelPositions.Builder builder = ModifyGuildChannelPositions.builder().guildId(guildId);
        botClient.getGuildChannels(guildId).toUni()
                .invoke(channel -> builder.addGuildChannelPosition(ModifyGuildChannelPositions.GuildChannelPosition.builder()
                        .id(channel.id())
                        .position(1)
                        .build()))
                .replaceWith(botClient.modifyGuildChannelPositions(builder.build()))
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
        botClient.listGuildMembers(ListGuildMembers.create(guildId))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(8)
    void testSearchGuildMembers(DiscordBotClient<?> botClient) {
        botClient.searchGuildMembers(SearchGuildMembers.create(guildId, "foo"))
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

        tokenUni.call(token -> botClient
                .addGuildMember(AddGuildMember.builder().guildId(guildId).userId(userId).accessToken(token).build()))
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
        botClient.modifyGuildMember(ModifyGuildMember.builder().guildId(guildId).userId(userId).nick("foo").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(12)
    void testModifyCurrentMember(DiscordBotClient<?> botClient) {
        botClient.modifyCurrentMember(ModifyCurrentMember.builder().guildId(guildId).nick("foo").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(13)
    void testGetGuildBans(DiscordBotClient<?> botClient) {
        botClient.getGuildBans(GetGuildBans.create(guildId))
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
        roleId = botClient.createGuildRole(CreateGuildRole.builder().guildId(guildId).name("foo").build())
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
        botClient.createGuildBan(CreateGuildBan.builder().guildId(guildId).userId(userId).build())
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
        ModifyGuildRolePositions modifyGuildRolePositions = ModifyGuildRolePositions.builder()
                .guildId(guildId)
                .addGuildRolePosition(ModifyGuildRolePositions.GuildRolePosition.builder()
                        .id(roleId)
                        .position(1)
                        .build())
                .build();

        botClient.modifyGuildRolePositions(modifyGuildRolePositions)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(23)
    void testModifyGuildRole(DiscordBotClient<?> botClient) {
        botClient.modifyGuildRole(ModifyGuildRole.builder().guildId(guildId).roleId(roleId).name("bar").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(24)
    void testModifyGuildMfaLevel(DiscordBotClient<?> botClient) {
        botClient.modifyGuildMfaLevel(guildId, Guild.MfaLevel.NONE, null)
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
        botClient.getGuildPruneCount(GetGuildPruneCount.create(guildId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(27)
    void testBeginGuildPrune(DiscordBotClient<?> botClient) {
        botClient.beginGuildPrune(BeginGuildPrune.create(guildId))
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
        botClient.modifyGuildWidget(ModifyGuildWidget.builder().guildId(guildId).enabled(true).build())
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
        botClient.modifyGuildWelcomeScreen(ModifyGuildWelcomeScreen.builder().guildId(guildId).description("foo").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("stage-channel")
    @Order(37)
    void testModifyCurrentUserVoiceState(DiscordBotClient<?> botClient) {
        ModifyCurrentUserVoiceState modifyCurrentUserVoiceState = ModifyCurrentUserVoiceState.builder()
                .guildId(guildId)
                .suppress(true)
                .build();

        botClient.modifyCurrentUserVoiceState(modifyCurrentUserVoiceState)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("stage-channel")
    @Order(38)
    void testModifyUserVoiceState(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId,
            @ConfigValue("DISCORD_STAGE_CHANNEL_ID") Snowflake channelId) {
        botClient.modifyUserVoiceState(guildId, userId, channelId, true)
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
