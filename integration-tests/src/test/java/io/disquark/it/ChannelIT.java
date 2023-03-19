package io.disquark.it;

import java.util.EnumSet;
import java.util.List;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.emoji.ReactionEmoji;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.json.forum.ForumThreadMessageParams;
import io.disquark.rest.json.oauth2.AccessToken;
import io.disquark.rest.oauth2.BearerTokenSource;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.oauth2.Scope;
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
class ChannelIT {
    private static final String ROBOT_EMOJI = "\uD83E\uDD16";

    private Snowflake channelId;
    private Snowflake messageId;
    private Snowflake dmChannelId;
    private Snowflake threadId;

    private Uni<Message> createMessage(DiscordBotClient<?> botClient, Snowflake channelId, String content) {
        return botClient.createMessage(channelId).withContent(content);
    }

    @BeforeAll
    void init(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        channelId = botClient.createGuildChannel(guildId, "foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(1)
    void testGetChannel(DiscordBotClient<?> botClient) {
        botClient.getChannel(channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("group-dm")
    @Order(2)
    void testModifyDmChannel(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CLIENT_ID") String clientId,
            @ConfigValue("DISCORD_CLIENT_SECRET") String clientSecret, @ConfigValue("DISCORD_USER_ID") Snowflake userId,
            @ConfigValue("DISCORD_USER_ID_2") Snowflake userId2) {
        Uni<String> tokenUni = BearerTokenSource.create(botClient.getVertx(), clientId, clientSecret)
                .fromClientCredentials(EnumSet.of(Scope.GDM_JOIN))
                .getToken()
                .map(AccessToken::accessToken);

        dmChannelId = botClient.createDm(userId2)
                .call(channel -> tokenUni.call(token -> botClient.groupDmAddRecipient(channel.id(), userId, token, null)))
                .call(channel -> botClient.modifyDmChannel(channel.id()).withName("foo"))
                .call(channel -> botClient.deleteOrCloseChannel(channel.id(), null))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(3)
    void testModifyGuildChannel(DiscordBotClient<?> botClient) {
        botClient.modifyGuildChannel(channelId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetChannelMessages(DiscordBotClient<?> botClient) {
        botClient.getChannelMessages(channelId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(5)
    void testTriggerTypingIndicator(DiscordBotClient<?> botClient) {
        messageId = botClient.triggerTypingIndicator(channelId)
                .replaceWith(createMessage(botClient, channelId, "Hello World!"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(6)
    void testGetChannelMessage(DiscordBotClient<?> botClient) {
        botClient.getChannelMessage(channelId, messageId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("announcement-channel")
    @Order(7)
    void testCrosspostMessage(DiscordBotClient<?> botClient,
            @ConfigValue("DISCORD_ANNOUNCEMENT_CHANNEL_ID") Snowflake announcementChannelId) {
        createMessage(botClient, announcementChannelId, "Hello World!")
                .flatMap(message -> botClient.crosspostMessage(announcementChannelId, messageId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(8)
    void testCreateReaction(DiscordBotClient<?> botClient) {
        botClient.createReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(9)
    void testDeleteOwnReaction(DiscordBotClient<?> botClient) {
        botClient.deleteOwnReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(10)
    void testDeleteUserReaction(DiscordBotClient<?> botClient) {
        botClient.getCurrentUser()
                .call(user -> botClient.createReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI)))
                .call(user -> botClient.deleteUserReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI), user.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(11)
    void testGetReactions(DiscordBotClient<?> botClient) {
        botClient.getReactions(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI))
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(12)
    void testDeleteAllReactions(DiscordBotClient<?> botClient) {
        botClient.deleteAllReactions(channelId, messageId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(13)
    void testDeleteAllReactionsForEmoji(DiscordBotClient<?> botClient) {
        botClient.deleteAllReactionsForEmoji(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(14)
    void testEditMessage(DiscordBotClient<?> botClient) {
        botClient.editMessage(channelId, messageId)
                .withContent("Goodbye Cruel World...")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(15)
    void testBulkDeleteMessages(DiscordBotClient<?> botClient) {
        Uni.combine()
                .all().unis(
                        createMessage(botClient, channelId, "One"),
                        createMessage(botClient, channelId, "Two"),
                        createMessage(botClient, channelId, "Three"))
                .combinedWith((m1, m2, m3) -> List.of(m1.id(), m2.id(), m3.id()))
                .flatMap(messages -> botClient.bulkDeleteMessages(channelId).withMessages(messages))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(16)
    void testEditChannelPermissions(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.editChannelPermissions(channelId, userId, Channel.Overwrite.Type.MEMBER)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(17)
    void testGetChannelInvites(DiscordBotClient<?> botClient) {
        botClient.getChannelInvites(channelId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(18)
    void testDeleteChannelPermission(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.deleteChannelPermission(channelId, userId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("announcement-channel")
    @Order(19)
    void testFollowAnnouncementChannel(DiscordBotClient<?> botClient,
            @ConfigValue("DISCORD_ANNOUNCEMENT_CHANNEL_ID") Snowflake announcementChannelId) {
        botClient.followAnnouncementChannel(announcementChannelId, channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(20)
    void testGetPinnedMessages(DiscordBotClient<?> botClient) {
        botClient.getPinnedMessages(channelId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(21)
    void testPinMessage(DiscordBotClient<?> botClient) {
        botClient.pinMessage(channelId, messageId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(22)
    void testUnpinMessage(DiscordBotClient<?> botClient) {
        botClient.unpinMessage(channelId, messageId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("group-dm")
    @Order(23)
    void testGroupDmRemoveRecipient(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.groupDmRemoveRecipient(dmChannelId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(24)
    void testStartThreadFromMessage(DiscordBotClient<?> botClient) {
        botClient.startThreadFromMessage(channelId, messageId, "foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(25)
    void testStartThreadWithoutMessage(DiscordBotClient<?> botClient) {
        threadId = botClient.startThreadWithoutMessage(channelId, "foo")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(26)
    void testModifyThread(DiscordBotClient<?> botClient) {
        botClient.modifyThread(threadId)
                .withName("bar")
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("forum-channel")
    @Order(27)
    void testStartThreadInForumChannel(DiscordBotClient<?> botClient,
            @ConfigValue("DISCORD_FORUM_CHANNEL_ID") Snowflake forumChannelId) {
        botClient.startThreadInForumChannel(forumChannelId, "foo")
                .withMessage(ForumThreadMessageParams.of().withContent("Hello World!"))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(28)
    void testAddThreadMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.addThreadMember(threadId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(29)
    void leaveThread(DiscordBotClient<?> botClient) {
        botClient.leaveThread(threadId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(30)
    void testJoinThread(DiscordBotClient<?> botClient) {
        botClient.joinThread(threadId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(31)
    void testGetThreadMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.getThreadMember(threadId, userId, false)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(32)
    void testRemoveThreadMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.removeThreadMember(threadId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Tag("members-intent")
    @Order(33)
    void testListThreadMembers(DiscordBotClient<?> botClient) {
        botClient.listThreadMembers(threadId)
                .collect().asList()
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(34)
    void testListPublicArchivedThreads(DiscordBotClient<?> botClient) {
        botClient.listPublicArchivedThreads(channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(35)
    void testListPrivateArchivedThreads(DiscordBotClient<?> botClient) {
        botClient.listPrivateArchivedThreads(channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(36)
    void testListJoinedPrivateArchivedThreads(DiscordBotClient<?> botClient) {
        botClient.listJoinedPrivateArchivedThreads(channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(37)
    void testDeleteMessage(DiscordBotClient<?> botClient) {
        botClient.deleteMessage(channelId, messageId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    @Order(38)
    void testDeleteOrCloseChannel(DiscordBotClient<?> botClient) {
        botClient.deleteOrCloseChannel(channelId, null).subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }
}
