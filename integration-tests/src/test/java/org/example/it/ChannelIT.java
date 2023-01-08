package org.example.it;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.example.it.config.ConfigValue;
import org.example.rest.DiscordBotClient;
import org.example.rest.emoji.ReactionEmoji;
import org.example.rest.oauth2.BearerTokenSource;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.*;
import org.example.rest.resources.channel.forum.StartThreadInForumChannel;
import org.example.rest.resources.channel.message.EditMessage;
import org.example.rest.resources.channel.message.GetReactions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.channel.message.StartThreadFromMessage;
import org.example.rest.resources.guild.CreateGuildChannel;
import org.example.rest.resources.oauth2.AccessToken;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class ChannelIT {
    private static final String ROBOT_EMOJI = "\uD83E\uDD16";

    private Snowflake channelId;
    private Snowflake messageId;
    private Snowflake dmChannelId;
    private Snowflake threadId;

    private Uni<Message> createMessage(DiscordBotClient<?> botClient, Snowflake channelId, String content) {
        return botClient.createMessage(CreateMessage.builder().channelId(channelId).content(content).build());
    }

    @BeforeAll
    void init(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_GUILD_ID") Snowflake guildId) {
        channelId = botClient.createGuildChannel(CreateGuildChannel.builder().guildId(guildId).name("foo").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(1)
    void testGetChannel(DiscordBotClient<?> botClient) {
        botClient.getChannel(channelId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Tag("group-dm")
    @Order(2)
    void testModifyDmChannel(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_CLIENT_ID") String clientId, @ConfigValue("DISCORD_CLIENT_SECRET") String clientSecret, @ConfigValue("DISCORD_USER_ID") Snowflake userId, @ConfigValue("DISCORD_USER_ID_2") Snowflake userId2) {
        Uni<String> tokenUni = BearerTokenSource.create(botClient.getVertx(), clientId, clientSecret)
                .fromClientCredentials()
                .getToken()
                .map(AccessToken::accessToken);

        ModifyDmChannel.Builder builder = ModifyDmChannel.builder().name("foo");

        dmChannelId = botClient.createDm(userId2)
                .call(channel -> tokenUni.call(token -> botClient.groupDmAddRecipient(channel.id(), userId, token, null)))
                .call(channel -> botClient.modifyChannel(builder.channelId(channel.id()).build()))
                .call(channel -> botClient.deleteOrCloseChannel(channel.id(), null))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Order(3)
    void testModifyGuildChannel(DiscordBotClient<?> botClient) {
        botClient.modifyChannel(ModifyGuildChannel.builder().channelId(channelId).name("bar").build())
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(4)
    void testGetChannelMessages(DiscordBotClient<?> botClient) {
        botClient.getChannelMessages(GetChannelMessages.create(channelId))
                .subscribe().withSubscriber(AssertSubscriber.create())
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
                .assertCompleted();
    }

    @Test
    @Tag("announcement-channel")
    @Order(7)
    void testCrosspostMessage(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_ANNOUNCEMENT_CHANNEL_ID") Snowflake announcementChannelId) {
        createMessage(botClient, announcementChannelId, "Hello World!")
                .flatMap(message -> botClient.crosspostMessage(announcementChannelId, messageId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(8)
    void testCreateReaction(DiscordBotClient<?> botClient) {
        botClient.createReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(9)
    void testDeleteOwnReaction(DiscordBotClient<?> botClient) {
        botClient.deleteOwnReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(10)
    void testDeleteUserReaction(DiscordBotClient<?> botClient) {
        botClient.getCurrentUser()
                .call(user -> botClient.createReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI)))
                .call(user -> botClient.deleteUserReaction(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI), user.id()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(11)
    void testGetReactions(DiscordBotClient<?> botClient) {
        botClient.getReactions(GetReactions.create(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI)))
                .subscribe().withSubscriber(AssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(12)
    void testDeleteAllReactions(DiscordBotClient<?> botClient) {
        botClient.deleteAllReactions(channelId, messageId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(13)
    void testDeleteAllReactionsForEmoji(DiscordBotClient<?> botClient) {
        botClient.deleteAllReactionsForEmoji(channelId, messageId, ReactionEmoji.create(ROBOT_EMOJI))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(14)
    void testEditMessage(DiscordBotClient<?> botClient) {
        EditMessage editMessage = EditMessage.builder()
                .channelId(channelId)
                .messageId(messageId)
                .content("Goodbye Cruel World...")
                .build();

        botClient.editMessage(editMessage).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(15)
    void testBulkDeleteMessages(DiscordBotClient<?> botClient) {
        BulkDeleteMessages.Builder builder = BulkDeleteMessages.builder().channelId(channelId);
        Uni.combine()
                .all().unis(
                        createMessage(botClient, channelId, "One"),
                        createMessage(botClient, channelId, "Two"),
                        createMessage(botClient, channelId, "Three"))
                .combinedWith((m1, m2, m3) -> List.of(m1.id(), m2.id(), m3.id()))
                .flatMap(messages -> botClient.bulkDeleteMessages(builder.messages(messages).build()))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(16)
    void testEditChannelPermissions(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        EditChannelPermissions editChannelPermissions = EditChannelPermissions.builder()
                .channelId(channelId)
                .overwriteId(userId)
                .build();

        botClient.editChannelPermissions(editChannelPermissions)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(17)
    void testGetChannelInvites(DiscordBotClient<?> botClient) {
        botClient.getChannelInvites(channelId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(18)
    void testDeleteChannelPermission(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.deleteChannelPermission(channelId, userId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Tag("announcement-channel")
    @Order(19)
    void testFollowAnnouncementChannel(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_ANNOUNCEMENT_CHANNEL_ID") Snowflake announcementChannelId) {
        botClient.followAnnouncementChannel(announcementChannelId, channelId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(20)
    void testGetPinnedMessages(DiscordBotClient<?> botClient) {
        botClient.getPinnedMessages(channelId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(21)
    void testPinMessage(DiscordBotClient<?> botClient) {
        botClient.pinMessage(channelId, messageId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(22)
    void testUnpinMessage(DiscordBotClient<?> botClient) {
        botClient.unpinMessage(channelId, messageId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Tag("group-dm")
    @Order(23)
    void testGroupDmRemoveRecipient(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.groupDmRemoveRecipient(dmChannelId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(24)
    void testStartThreadFromMessage(DiscordBotClient<?> botClient) {
        StartThreadFromMessage startThreadFromMessage = StartThreadFromMessage.builder()
                .channelId(channelId)
                .messageId(messageId)
                .name("foo")
                .build();

        botClient.startThreadFromMessage(startThreadFromMessage)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(25)
    void testStartThreadWithoutMessage(DiscordBotClient<?> botClient) {
        StartThreadWithoutMessage startThreadWithoutMessage = StartThreadWithoutMessage.builder()
                .channelId(channelId)
                .name("foo")
                .build();

        threadId = botClient.startThreadWithoutMessage(startThreadWithoutMessage)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .getItem()
                .id();
    }

    @Test
    @Tag("forum-channel")
    @Order(26)
    void testStartThreadInForumChannel(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_FORUM_CHANNEL_ID") Snowflake forumChannelId) {
        StartThreadInForumChannel startThreadInForumChannel = StartThreadInForumChannel.builder()
                .channelId(forumChannelId)
                .message(StartThreadInForumChannel.ForumThreadMessageParams.builder().content("Hello World!").build())
                .build();

        botClient.startThreadInForumChannel(startThreadInForumChannel)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(27)
    void testAddThreadMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.addThreadMember(threadId, userId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(28)
    void leaveThread(DiscordBotClient<?> botClient) {
        botClient.leaveThread(threadId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(29)
    void testJoinThread(DiscordBotClient<?> botClient) {
        botClient.joinThread(threadId).subscribe().withSubscriber(UniAssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(30)
    void testGetThreadMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.getThreadMember(threadId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(31)
    void testRemoveThreadMember(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.removeThreadMember(threadId, userId)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(32)
    void testListThreadMembers(DiscordBotClient<?> botClient) {
        botClient.listThreadMembers(threadId).subscribe().withSubscriber(AssertSubscriber.create()).assertCompleted();
    }

    @Test
    @Order(33)
    void testListPublicArchivedThreads(DiscordBotClient<?> botClient) {
        botClient.listPublicArchivedThreads(ListThreads.create(channelId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(34)
    void testListPrivateArchivedThreads(DiscordBotClient<?> botClient) {
        botClient.listPrivateArchivedThreads(ListThreads.create(channelId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(35)
    void testListJoinedPrivateArchivedThreads(DiscordBotClient<?> botClient) {
        botClient.listJoinedPrivateArchivedThreads(ListThreads.create(channelId))
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(36)
    void testDeleteMessage(DiscordBotClient<?> botClient) {
        botClient.deleteMessage(channelId, messageId, null)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }

    @Test
    @Order(37)
    void testDeleteOrCloseChannel(DiscordBotClient<?> botClient) {
        botClient.deleteOrCloseChannel(channelId, null).subscribe()
                .withSubscriber(UniAssertSubscriber.create())
                .assertCompleted();
    }
}
