package org.example.it;

import org.example.it.extension.SomeExtension2;
import org.example.rest.DiscordBotClient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class ChannelIT {

    @Test
    void testGetChannel(DiscordBotClient<?> botClient) {

    }

    @Test
    void testModifyChannel(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteOrCloseChannel(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetChannelMessages(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetChannelMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testCreateMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testCrosspostMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testCreateReaction(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteOwnReaction(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteUserReaction(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetReactions(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteAllReactions(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteAllReactionsForEmoji(DiscordBotClient<?> botClient) {

    }

    @Test
    void testEditMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testBulkDeleteMessages(DiscordBotClient<?> botClient) {

    }

    @Test
    void testEditChannelPermissions(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetChannelInvites(DiscordBotClient<?> botClient) {

    }

    @Test
    void testDeleteChannelPermission(DiscordBotClient<?> botClient) {

    }

    @Test
    void testFollowAnnouncementChannel(DiscordBotClient<?> botClient) {

    }

    @Test
    void testTriggerTypingIndicator(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetPinnedMessages(DiscordBotClient<?> botClient) {

    }

    @Test
    void testPinMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testUnpinMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGroupDmAddRecipient(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGroupDmRemoveRecipient(DiscordBotClient<?> botClient) {

    }

    @Test
    void testStartThreadFromMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testStartThreadWithoutMessage(DiscordBotClient<?> botClient) {

    }

    @Test
    void testStartThreadInForumChannel(DiscordBotClient<?> botClient) {

    }

    @Test
    void testJoinThread(DiscordBotClient<?> botClient) {

    }

    @Test
    void testAddThreadMember(DiscordBotClient<?> botClient) {

    }

    @Test
    void testRemoveThreadMember(DiscordBotClient<?> botClient) {

    }

    @Test
    void testGetThreadMember(DiscordBotClient<?> botClient) {

    }

    @Test
    void testListThreadMembers(DiscordBotClient<?> botClient) {

    }

    @Test
    void testListPublicArchivedThreads(DiscordBotClient<?> botClient) {

    }

    @Test
    void testListPrivateArchivedThreads(DiscordBotClient<?> botClient) {

    }

    @Test
    void testListJoinedPrivateArchivedThreads(DiscordBotClient<?> botClient) {

    }
}
