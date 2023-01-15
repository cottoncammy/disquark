package org.example.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.request.*;
import org.example.rest.resources.*;
import org.example.rest.resources.application.Application;
import org.example.rest.resources.application.ApplicationRoleConnectionMetadata;
import org.example.rest.resources.application.UpdateApplicationRoleConnectionMetadataRecords;
import org.example.rest.resources.auditlog.AuditLog;
import org.example.rest.resources.auditlog.GetGuildAuditLog;
import org.example.rest.resources.automod.AutoModerationRule;
import org.example.rest.resources.automod.CreateAutoModerationRule;
import org.example.rest.resources.automod.ModifyAutoModerationRule;
import org.example.rest.resources.channel.*;
import org.example.rest.resources.channel.forum.StartThreadInForumChannel;
import org.example.rest.resources.channel.message.*;
import org.example.rest.resources.channel.thread.*;
import org.example.rest.resources.emoji.CreateGuildEmoji;
import org.example.rest.resources.emoji.Emoji;
import org.example.rest.resources.emoji.ModifyGuildEmoji;
import org.example.rest.resources.guild.*;
import org.example.rest.resources.guild.prune.BeginGuildPrune;
import org.example.rest.resources.guild.prune.GetGuildPruneCount;
import org.example.rest.resources.guild.prune.GuildPruneResponse;
import org.example.rest.resources.guild.scheduledevent.CreateGuildScheduledEvent;
import org.example.rest.resources.guild.scheduledevent.GetGuildScheduledEventUsers;
import org.example.rest.resources.guild.scheduledevent.GuildScheduledEvent;
import org.example.rest.resources.guild.scheduledevent.ModifyGuildScheduledEvent;
import org.example.rest.resources.guild.template.CreateGuildFromGuildTemplate;
import org.example.rest.resources.guild.template.CreateGuildTemplate;
import org.example.rest.resources.guild.template.GuildTemplate;
import org.example.rest.resources.guild.template.ModifyGuildTemplate;
import org.example.rest.resources.invite.GetInvite;
import org.example.rest.resources.invite.Invite;
import org.example.rest.resources.oauth2.AccessToken;
import org.example.rest.resources.oauth2.TokenType;
import org.example.rest.resources.permissions.Role;
import org.example.rest.resources.stageinstance.CreateStageInstance;
import org.example.rest.resources.stageinstance.ModifyStageInstance;
import org.example.rest.resources.stageinstance.StageInstance;
import org.example.rest.resources.sticker.CreateGuildSticker;
import org.example.rest.resources.sticker.ModifyGuildSticker;
import org.example.rest.resources.sticker.ListNitroStickerPacksResponse;
import org.example.rest.resources.sticker.Sticker;
import org.example.rest.resources.user.CreateDm;
import org.example.rest.resources.user.ModifyCurrentUser;
import org.example.rest.resources.user.User;
import org.example.rest.resources.voice.VoiceRegion;
import org.example.rest.resources.webhook.CreateWebhook;
import org.example.rest.resources.webhook.ModifyWebhook;
import org.example.rest.resources.webhook.Webhook;
import org.example.rest.response.Response;
import org.example.rest.emoji.ReactionEmoji;

import javax.annotation.Nullable;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

public class DiscordBotClient<T extends Response> extends AuthenticatedDiscordClient<T> {

    public static <T extends Response> Builder<T> builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder<>(requireNonNull(vertx, "vertx"), requireNonNull(tokenSource, "tokenSource"));
    }

    public static <T extends Response> Builder<T> builder(Vertx vertx, String token) {
        return builder(vertx, BotToken.create(token));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, AccessTokenSource tokenSource) {
        return (DiscordBotClient<T>) builder(vertx, tokenSource).build();
    }

    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, String token) {
        return create(vertx, BotToken.create(token));
    }

    private DiscordBotClient(Vertx vertx, Requester<T> requester, DiscordInteractionsClient.Options interactionsClientOptions) {
        super(vertx, requester, interactionsClientOptions);
    }

    @Override
    protected DiscordInteractionsClient<T> buildInteractionsClient() {
        String verifyKey = interactionsClientOptions.getVerifyKey();
        if (verifyKey == null) {
            verifyKey = getCurrentBotApplicationInformation().map(Application::verifyKey).await().indefinitely();
        }

        return buildInteractionsClient(DiscordInteractionsClient.builder(vertx, verifyKey));
    }

    public Multi<ApplicationRoleConnectionMetadata> getApplicationRoleConnectionMetadataRecords(Snowflake applicationId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/role-connections/metadata", variables("application.id", requireNonNull(applicationId, "applicationId").getValue())))
                .flatMap(res -> res.as(ApplicationRoleConnectionMetadata[].class))
                .onItem().disjoint();
    }

    public Multi<ApplicationRoleConnectionMetadata> updateApplicationRoleConnectionMetadataRecords(UpdateApplicationRoleConnectionMetadataRecords updateApplicationRoleConnectionMetadataRecords) {
        return requester.request(requireNonNull(updateApplicationRoleConnectionMetadataRecords, "updateApplicationRoleConnectionMetadataRecords").asRequest())
                .flatMap(res -> res.as(ApplicationRoleConnectionMetadata[].class))
                .onItem().disjoint();
    }

    public Uni<AuditLog> getGuildAuditLog(GetGuildAuditLog getGuildAuditLog) {
        return requester.request(requireNonNull(getGuildAuditLog, "getGuildAuditLog").asRequest())
                .flatMap(res -> res.as(AuditLog.class));
    }

    public Multi<AutoModerationRule> listAutoModerationRulesForGuild(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/auto-moderation/rules", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(AutoModerationRule[].class))
                .onItem().disjoint();
    }

    public Uni<AutoModerationRule> getAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "auto_moderation_rule.id", requireNonNull(autoModerationRuleId, "autoModerationRuleId").getValue())))
                .flatMap(res -> res.as(AutoModerationRule.class));
    }

    public Uni<AutoModerationRule> createAutoModerationRule(CreateAutoModerationRule createAutoModerationRule) {
        return requester.request(requireNonNull(createAutoModerationRule, "createAutoModerationRule").asRequest())
                .flatMap(res -> res.as(AutoModerationRule.class));
    }

    public Uni<AutoModerationRule> modifyAutoModerationRule(ModifyAutoModerationRule modifyAutoModerationRule) {
        return requester.request(requireNonNull(modifyAutoModerationRule, "modifyAutoModerationRule").asRequest())
                .flatMap(res -> res.as(AutoModerationRule.class));
    }

    public Uni<Void> deleteAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "auto_moderation_rule.id", requireNonNull(autoModerationRuleId, "autoModerationRuleId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Channel> getChannel(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> modifyChannel(ModifyDmChannel modifyDmChannel) {
        return requester.request(requireNonNull(modifyDmChannel, "modifyDmChannel").asRequest())
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> modifyChannel(ModifyGuildChannel modifyGuildChannel) {
        return requester.request(requireNonNull(modifyGuildChannel, "modifyGuildChannel").asRequest())
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> modifyChannel(ModifyThread modifyThread) {
        return requester.request(requireNonNull(modifyThread, "modifyThread").asRequest())
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> deleteOrCloseChannel(Snowflake channelId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue()), auditLogReason))
                .flatMap(res -> res.as(Channel.class));
    }

    public Multi<Message> getChannelMessages(GetChannelMessages getChannelMessages) {
        return requester.request(requireNonNull(getChannelMessages, "getChannelMessages").asRequest())
                .flatMap(res -> res.as(Message[].class)).onItem().disjoint();
    }

    public Uni<Message> getChannelMessage(Snowflake channelId, Snowflake messageId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/messages/{message.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue())))
                .flatMap(res -> res.as(Message.class));
    }

    public Uni<Message> createMessage(CreateMessage createMessage) {
        return requester.request(requireNonNull(createMessage, "createMessage").asRequest())
                .flatMap(res -> res.as(Message.class));
    }

    public Uni<Message> crosspostMessage(Snowflake channelId, Snowflake messageId) {
        return requester.request(new EmptyRequest(HttpMethod.POST, "/channels/{channel.id}/messages/{message.id}/crosspost", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue())))
                .flatMap(res -> res.as(Message.class));
    }

    public Uni<Void> createReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/@me", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue(), "emoji", requireNonNull(emoji, "emoji").getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> deleteOwnReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/@me", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue(), "emoji", requireNonNull(emoji, "emoji").getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> deleteUserReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/{user.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue(), "emoji", requireNonNull(emoji, "emoji").getValue(), "user.id", requireNonNull(userId, "userId").getValue())))
                .replaceWithVoid();
    }

    public Multi<User> getReactions(GetReactions getReactions) {
        return requester.request(requireNonNull(getReactions, "getReactions").asRequest())
                .flatMap(res -> res.as(User[].class)).onItem().disjoint();
    }

    public Uni<Void> deleteAllReactions(Snowflake channelId, Snowflake messageId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> deleteAllReactionsForEmoji(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue(), "emoji", requireNonNull(emoji, "emoji").getValue())))
                .replaceWithVoid();
    }

    public Uni<Message> editMessage(EditMessage editMessage) {
        return requester.request(requireNonNull(editMessage, "editMessage").asRequest())
                .flatMap(res -> res.as(Message.class));
    }

    public Uni<Void> deleteMessage(Snowflake channelId, Snowflake messageId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> bulkDeleteMessages(BulkDeleteMessages bulkDeleteMessages) {
        return requester.request(requireNonNull(bulkDeleteMessages, "bulkDeleteMessages").asRequest()).replaceWithVoid();
    }

    public Uni<Void> editChannelPermissions(EditChannelPermissions editChannelPermissions) {
        return requester.request(requireNonNull(editChannelPermissions, "editChannelPermissions").asRequest())
                .replaceWithVoid();
    }

    public Multi<Invite> getChannelInvites(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/invites", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .flatMap(res -> res.as(Invite[].class))
                .onItem().disjoint();
    }

    public Uni<Invite> createChannelInvite(CreateChannelInvite createChannelInvite) {
        return requester.request(requireNonNull(createChannelInvite, "createChannelInvite").asRequest())
                .flatMap(res -> res.as(Invite.class));
    }

    public Uni<Void> deleteChannelPermission(Snowflake channelId, Snowflake overwriteId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/permissions/{overwrite.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "overwrite.id", requireNonNull(overwriteId, "overwriteId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<FollowedChannel> followAnnouncementChannel(Snowflake channelId, Snowflake webhookChannelId) {
        return requester.request(FollowAnnouncementChannel.create(channelId, webhookChannelId).asRequest())
                .flatMap(res -> res.as(FollowedChannel.class));
    }

    public Uni<Void> triggerTypingIndicator(Snowflake channelId) {
        return requester.request(new EmptyRequest(HttpMethod.POST, "/channels/{channel.id}/typing", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .replaceWithVoid();
    }

    public Multi<Message> getPinnedMessages(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/pins", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .flatMap(res -> res.as(Message[].class))
                .onItem().disjoint();
    }

    public Uni<Void> pinMessage(Snowflake channelId, Snowflake messageId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/pins/{message.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> unpinMessage(Snowflake channelId, Snowflake messageId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/pins/{message.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "message.id", requireNonNull(messageId, "messageId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> groupDmAddRecipient(Snowflake channelId, Snowflake userId, String accessToken, String nick) {
        return requester.request(GroupDmAddRecipient.create(channelId, userId, accessToken, nick).asRequest())
                .replaceWithVoid();
    }

    public Uni<Void> groupDmRemoveRecipient(Snowflake channelId, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/recipients/users/{user.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "user.id", requireNonNull(userId, "userId").getValue())))
                .replaceWithVoid();
    }

    public Uni<Channel> startThreadFromMessage(StartThreadFromMessage startThreadFromMessage) {
        return requester.request(requireNonNull(startThreadFromMessage, "startThreadFromMessage").asRequest())
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> startThreadWithoutMessage(StartThreadWithoutMessage startThreadWithoutMessage) {
        return requester.request(requireNonNull(startThreadWithoutMessage, "startThreadWithoutMessage").asRequest())
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> startThreadInForumChannel(StartThreadInForumChannel startThreadInForumChannel) {
        return requester.request(requireNonNull(startThreadInForumChannel, "startThreadInForumChannel").asRequest())
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Void> joinThread(Snowflake channelId) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/thread-members/@me", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> addThreadMember(Snowflake channelId, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/thread-members/{user.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "user.id", requireNonNull(userId, "userId").getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> leaveThread(Snowflake channelId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/thread-members/@me", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> removeThreadMember(Snowflake channelId, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/thread-members/{user.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "user.id", requireNonNull(userId, "userId").getValue())))
                .replaceWithVoid();
    }

    public Uni<ThreadMember> getThreadMember(Snowflake channelId, Snowflake userId, boolean withMember) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/thread-members/{user.id}{?with_member}", variables("channel.id", requireNonNull(channelId, "channelId").getValue(), "user.id", requireNonNull(userId, "userId").getValue(), "with_member", withMember)))
                .flatMap(res -> res.as(ThreadMember.class));
    }

    public Multi<ThreadMember> listThreadMembers(ListThreadMembers listThreadMembers) {
        return requester.request(requireNonNull(listThreadMembers, "listThreadMembers").asRequest())
                .flatMap(res -> res.as(ThreadMember[].class))
                .onItem().disjoint();
    }

    private Uni<ListThreadsResult> listThreads(ListThreads listThreads, String uri) {
        JsonObject json = JsonObject.of("channel.id", requireNonNull(listThreads, "listThreads").channelId().getValue());
        if (listThreads.limit().isPresent()) {
            json.put("limit", listThreads.limit().getAsInt());
        }

        if (listThreads.before().isPresent()) {
            json.put("before", ISO_DATE_TIME.format(listThreads.before().get()));
        }

        return requester.request(new EmptyRequest(uri, Variables.variables(json)))
                .flatMap(res -> res.as(ListThreadsResult.class));
    }

    public Uni<ListThreadsResult> listPublicArchivedThreads(ListThreads listThreads) {
        return listThreads(listThreads, "/channels/{channel.id}/threads/archived/public{?before,limit}");
    }

    public Uni<ListThreadsResult> listPrivateArchivedThreads(ListThreads listThreads) {
        return listThreads(listThreads, "/channels/{channel.id}/threads/archived/private{?before,limit}");
    }

    public Uni<ListThreadsResult> listJoinedPrivateArchivedThreads(ListThreads listThreads) {
        return listThreads(listThreads, "/channels/{channel.id}/users/@me/threads/archived/private{?before,limit}");
    }

    public Multi<Emoji> listGuildEmojis(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/emojis", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Emoji[].class))
                .onItem().disjoint();
    }

    public Uni<Emoji> getGuildEmoji(Snowflake guildId, Snowflake emojiId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/emojis/{emoji.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "emoji.id", requireNonNull(emojiId, "emojiId").getValue())))
                .flatMap(res -> res.as(Emoji.class));
    }

    public Uni<Emoji> createGuildEmoji(CreateGuildEmoji createGuildEmoji) {
        return requester.request(requireNonNull(createGuildEmoji, "createGuildEmoji").asRequest())
                .flatMap(res -> res.as(Emoji.class));
    }

    public Uni<Emoji> modifyGuildEmoji(ModifyGuildEmoji modifyGuildEmoji) {
        return requester.request(requireNonNull(modifyGuildEmoji, "modifyGuildEmoji").asRequest())
                .flatMap(res -> res.as(Emoji.class));
    }

    public Uni<Void> deleteGuildEmoji(Snowflake guildId, Snowflake emojiId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/emojis/{emoji.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "emoji.id", requireNonNull(emojiId, "emojiId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Guild> createGuild(CreateGuild createGuild) {
        return requester.request(requireNonNull(createGuild, "createGuild").asRequest())
                .flatMap(res -> res.as(Guild.class));
    }

    public Uni<Guild> getGuild(Snowflake guildId, boolean withCounts) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}{?with_counts}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "with_counts", withCounts)))
                .flatMap(res -> res.as(Guild.class));
    }

    public Uni<Guild.Preview> getGuildPreview(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/preview", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Guild.Preview.class));
    }

    public Uni<Guild> modifyGuild(ModifyGuild modifyGuild) {
        return requester.request(requireNonNull(modifyGuild, "modifyGuild").asRequest())
                .flatMap(res -> res.as(Guild.class));
    }

    public Uni<Void> deleteGuild(Snowflake guildId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .replaceWithVoid();
    }

    public Multi<Channel> getGuildChannels(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/channels", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Channel[].class))
                .onItem().disjoint();
    }

    public Uni<Channel> createGuildChannel(CreateGuildChannel createGuildChannel) {
        return requester.request(requireNonNull(createGuildChannel, "createGuildChannel").asRequest())
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Void> modifyGuildChannelPositions(ModifyGuildChannelPositions modifyGuildChannelPositions) {
        return requester.request(requireNonNull(modifyGuildChannelPositions, "modifyGuildChannelPositions").asRequest())
                .replaceWithVoid();
    }

    public Uni<ListThreadsResult> listActiveGuildThreads(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/threads/active", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(ListThreadsResult.class));
    }

    public Uni<Guild.Member> getGuildMember(Snowflake guildId, Snowflake userId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/members/{user.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "user.id", requireNonNull(userId, "userId").getValue())))
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Multi<Guild.Member> listGuildMembers(ListGuildMembers listGuildMembers) {
        return requester.request(requireNonNull(listGuildMembers, "listGuildMembers").asRequest())
                .flatMap(res -> res.as(Guild.Member[].class)).onItem().disjoint();
    }

    public Multi<Guild.Member> searchGuildMembers(SearchGuildMembers searchGuildMembers) {
        return requester.request(requireNonNull(searchGuildMembers, "searchGuildMembers").asRequest())
                .flatMap(res -> res.as(Guild.Member[].class)).onItem().disjoint();
    }

    public Uni<Guild.Member> addGuildMember(AddGuildMember addGuildMember) {
        return requester.request(requireNonNull(addGuildMember, "addGuildMember").asRequest())
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Uni<Guild.Member> modifyGuildMember(ModifyGuildMember modifyGuildMember) {
        return requester.request(requireNonNull(modifyGuildMember, "modifyGuildMember").asRequest())
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Uni<Guild.Member> modifyCurrentMember(ModifyCurrentMember modifyCurrentMember) {
        return requester.request(requireNonNull(modifyCurrentMember, "modifyCurrentMember").asRequest())
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Uni<Void> addGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/guilds/{guild.id}/members/{user.id}/roles/{role.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "user.id", requireNonNull(userId, "userId").getValue(), "role.id", requireNonNull(roleId, "roleId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> removeGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/members/{user.id}/roles/{role.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "user.id", requireNonNull(userId, "userId").getValue(), "role.id", requireNonNull(roleId, "roleId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> removeGuildMember(Snowflake guildId, Snowflake userId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/members/{user.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "user.id", requireNonNull(userId, "userId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Multi<Guild.Ban> getGuildBans(GetGuildBans getGuildBans) {
        return requester.request(requireNonNull(getGuildBans, "getGuildBans").asRequest())
                .flatMap(res -> res.as(Guild.Ban[].class)).onItem().disjoint();
    }

    public Uni<Guild.Ban> getGuildBan(Snowflake guildId, Snowflake userId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/bans/{user.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "user.id", requireNonNull(userId, "userId").getValue())))
                .flatMap(res -> res.as(Guild.Ban.class));
    }

    public Uni<Void> createGuildBan(CreateGuildBan createGuildBan) {
        return requester.request(requireNonNull(createGuildBan, "createGuildBan").asRequest()).replaceWithVoid();
    }

    public Uni<Void> removeGuildBan(Snowflake guildId, Snowflake userId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/bans/{user.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "user.id", requireNonNull(userId, "userId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Multi<Role> getGuildRoles(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/roles", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Role[].class))
                .onItem().disjoint();
    }

    public Uni<Role> createGuildRole(CreateGuildRole createGuildRole) {
        return requester.request(requireNonNull(createGuildRole, "createGuildRole").asRequest())
                .flatMap(res -> res.as(Role.class));
    }

    public Multi<Role> modifyGuildRolePositions(ModifyGuildRolePositions modifyGuildRolePositions) {
        return requester.request(requireNonNull(modifyGuildRolePositions, "modifyGuildRolePositions").asRequest())
                .flatMap(res -> res.as(Role[].class)).onItem().disjoint();
    }

    public Uni<Role> modifyGuildRole(ModifyGuildRole modifyGuildRole) {
        return requester.request(requireNonNull(modifyGuildRole, "modifyGuildRole").asRequest())
                .flatMap(res -> res.as(Role.class));
    }

    public Uni<Guild.MfaLevel> modifyGuildMfaLevel(Snowflake guildId, Guild.MfaLevel level, @Nullable String auditLogReason) {
        return requester.request(ModifyGuildMfaLevel.create(guildId, level, auditLogReason).asRequest())
                .flatMap(res -> res.as(ModifyGuildMfaLevel.Response.class))
                .map(ModifyGuildMfaLevel.Response::getLevel);
    }

    public Uni<Void> deleteGuildRole(Snowflake guildId, Snowflake roleId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/roles/{role.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "role.id", requireNonNull(roleId, "roleId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Integer> getGuildPruneCount(GetGuildPruneCount getGuildPruneCount) {
        return requester.request(requireNonNull(getGuildPruneCount, "getGuildPruneCount").asRequest())
                .flatMap(res -> res.as(GuildPruneResponse.class))
                .flatMap(result -> Uni.createFrom().optional(result.getPruned()));
    }

    public Uni<Integer> beginGuildPrune(BeginGuildPrune beginGuildPrune) {
        return requester.request(requireNonNull(beginGuildPrune, "beginGuildPrune").asRequest())
                .flatMap(res -> res.as(GuildPruneResponse.class))
                .flatMap(result -> Uni.createFrom().optional(result.getPruned()));
    }

    public Multi<VoiceRegion> getGuildVoiceRegions(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/regions", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(VoiceRegion[].class))
                .onItem().disjoint();
    }

    public Multi<Invite> getGuildInvites(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/invites", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Invite[].class))
                .onItem().disjoint();
    }

    public Multi<Guild.Integration> getGuildIntegrations(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/integrations", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Guild.Integration[].class))
                .onItem().disjoint();
    }

    public Uni<Void> deleteGuildIntegration(Snowflake guildId, Snowflake integrationId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/integrations/{integration.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "integration.id", requireNonNull(integrationId, "integrationId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Guild.WidgetSettings> getGuildWidgetSettings(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/widget", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Guild.WidgetSettings.class));
    }

    public Uni<Guild.Widget> modifyGuildWidget(ModifyGuildWidget modifyGuildWidget) {
        return requester.request(requireNonNull(modifyGuildWidget, "modifyGuildWidget").asRequest())
                .flatMap(res -> res.as(Guild.Widget.class));
    }

    public Uni<Guild.Widget> getGuildWidget(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/widget.json", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Guild.Widget.class));
    }

    public Uni<Invite> getGuildVanityUrl(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/vanity-url", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Invite.class));
    }

    public Uni<Guild.WelcomeScreen> getGuildWelcomeScreen(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/welcome-screen", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Guild.WelcomeScreen.class));
    }

    public Uni<Guild.WelcomeScreen> modifyGuildWelcomeScreen(ModifyGuildWelcomeScreen modifyGuildWelcomeScreen) {
        return requester.request(requireNonNull(modifyGuildWelcomeScreen, "modifyGuildWelcomeScreen").asRequest())
                .flatMap(res -> res.as(Guild.WelcomeScreen.class));
    }

    public Uni<Void> modifyCurrentUserVoiceState(ModifyCurrentUserVoiceState modifyCurrentUserVoiceState) {
        return requester.request(requireNonNull(modifyCurrentUserVoiceState, "modifyCurrentUserVoiceState").asRequest())
                .replaceWithVoid();
    }

    public Uni<Void> modifyUserVoiceState(Snowflake guildId, Snowflake userId, Snowflake channelId, boolean suppress) {
        return requester.request(ModifyUserVoiceState.create(guildId, userId, channelId, suppress).asRequest())
                .replaceWithVoid();
    }

    public Multi<GuildScheduledEvent> listScheduledEventsForGuild(Snowflake guildId, boolean withUserCount) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/scheduled-events{?with_user_count}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "with_user_count", withUserCount)))
                .flatMap(res -> res.as(GuildScheduledEvent[].class))
                .onItem().disjoint();
    }

    public Uni<GuildScheduledEvent> createGuildScheduledEvent(CreateGuildScheduledEvent createGuildScheduledEvent) {
        return requester.request(requireNonNull(createGuildScheduledEvent, "createGuildScheduledEvent").asRequest())
                .flatMap(res -> res.as(GuildScheduledEvent.class));
    }

    public Uni<GuildScheduledEvent> getGuildScheduledEvent(Snowflake guildId, Snowflake guildScheduledEventId, boolean withUserCount) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}{?with_user_count}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "guild_scheduled_event.id", requireNonNull(guildScheduledEventId, "guildScheduledEventId").getValue(), "with_user_count", withUserCount)))
                .flatMap(res -> res.as(GuildScheduledEvent.class));
    }

    public Uni<GuildScheduledEvent> modifyGuildScheduledEvent(ModifyGuildScheduledEvent modifyGuildScheduledEvent) {
        return requester.request(requireNonNull(modifyGuildScheduledEvent, "modifyGuildScheduledEvent").asRequest())
                .flatMap(res -> res.as(GuildScheduledEvent.class));
    }

    public Uni<Void> deleteGuildScheduledEvent(Snowflake guildId, Snowflake guildScheduledEventId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "guild_scheduled_event.id", requireNonNull(guildScheduledEventId, "guildScheduledEventId").getValue())))
                .replaceWithVoid();
    }

    public Multi<GuildScheduledEvent.User> getGuildScheduledEventUsers(GetGuildScheduledEventUsers getGuildScheduledEventUsers) {
        return requester.request(requireNonNull(getGuildScheduledEventUsers, "getGuildScheduledEventUsers").asRequest())
                .flatMap(res -> res.as(GuildScheduledEvent.User[].class))
                .onItem().disjoint();
    }

    public Uni<GuildTemplate> getGuildTemplate(String templateCode) {
        return requester.request(new EmptyRequest("/guilds/templates/{template.code}", variables("template.code", requireNonNull(templateCode, "templateCode"))))
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<Guild> createGuildFromGuildTemplate(CreateGuildFromGuildTemplate createGuildFromGuildTemplate) {
        return requester.request(requireNonNull(createGuildFromGuildTemplate, "createGuildFromGuildTemplate").asRequest())
                .flatMap(res -> res.as(Guild.class));
    }

    public Multi<GuildTemplate> getGuildTemplates(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/templates", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(GuildTemplate[].class))
                .onItem().disjoint();
    }

    public Uni<GuildTemplate> createGuildTemplate(CreateGuildTemplate createGuildTemplate) {
        return requester.request(requireNonNull(createGuildTemplate, "createGuildTemplate").asRequest())
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<GuildTemplate> syncGuildTemplate(Snowflake guildId, String templateCode) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/guilds/{guild.id}/templates/{template.code}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "template.code", requireNonNull(templateCode, "templateCode"))))
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<GuildTemplate> modifyGuildTemplate(ModifyGuildTemplate modifyGuildTemplate) {
        return requester.request(requireNonNull(modifyGuildTemplate, "modifyGuildTemplate").asRequest())
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<Void> deleteGuildTemplate(Snowflake guildId, String templateCode) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/templates/{template.code}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "template.code", requireNonNull(templateCode, "templateCode"))))
                .replaceWithVoid();
    }

    public Uni<Invite> getInvite(GetInvite getInvite) {
        return requester.request(requireNonNull(getInvite, "getInvite").asRequest())
                .flatMap(res -> res.as(Invite.class));
    }

    public Uni<Invite> deleteInvite(String inviteCode, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/invites/{invite.code}", variables("invite.code", requireNonNull(inviteCode, "inviteCode")), auditLogReason))
                .flatMap(res -> res.as(Invite.class));
    }

    public Uni<StageInstance> createStageInstance(CreateStageInstance createStageInstance) {
        return requester.request(requireNonNull(createStageInstance, "createStageInstance").asRequest())
                .flatMap(res -> res.as(StageInstance.class));
    }

    public Uni<StageInstance> getStageInstance(Snowflake channelId) {
        return requester.request(new EmptyRequest("/stage-instances/{channel.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .flatMap(res -> res.as(StageInstance.class));
    }

    public Uni<StageInstance> modifyStageInstance(ModifyStageInstance modifyStageInstance) {
        return requester.request(requireNonNull(modifyStageInstance, "modifyStageInstance").asRequest())
                .flatMap(res -> res.as(StageInstance.class));
    }

    public Uni<Void> deleteStageInstance(Snowflake channelId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/stage-instances/{channel.id}", variables("channel.id", requireNonNull(channelId, "channelId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Sticker> getSticker(Snowflake stickerId) {
        return requester.request(new EmptyRequest("/stickers/{sticker.id}", variables("sticker.id", requireNonNull(stickerId, "stickerId").getValue())))
                .flatMap(res -> res.as(Sticker.class));
    }

    public Multi<Sticker.Pack> listNitroStickerPacks() {
        return requester.request(new EmptyRequest("/sticker-packs"))
                .flatMap(res -> res.as(ListNitroStickerPacksResponse.class))
                .map(ListNitroStickerPacksResponse::getStickerPacks)
                .onItem().disjoint();
    }

    public Multi<Sticker> listGuildStickers(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/stickers", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Sticker[].class))
                .onItem().disjoint();
    }

    public Uni<Sticker> getGuildSticker(Snowflake guildId, Snowflake stickerId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/stickers/{sticker.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "sticker.id", requireNonNull(stickerId, "stickerId").getValue())))
                .flatMap(res -> res.as(Sticker.class));
    }

    public Uni<Sticker> createGuildSticker(CreateGuildSticker createGuildSticker) {
        return requester.request(requireNonNull(createGuildSticker, "createGuildSticker").asRequest())
                .flatMap(res -> res.as(Sticker.class));
    }

    public Uni<Sticker> modifyGuildSticker(ModifyGuildSticker modifyGuildSticker) {
        return requester.request(requireNonNull(modifyGuildSticker, "modifyGuildSticker").asRequest())
                .flatMap(res -> res.as(Sticker.class));
    }

    public Uni<Void> deleteGuildSticker(Snowflake guildId, Snowflake stickerId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/stickers/{sticker.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "sticker.id", requireNonNull(stickerId, "stickerId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<User> modifyCurrentUser(ModifyCurrentUser modifyCurrentUser) {
        return requester.request(requireNonNull(modifyCurrentUser, "modifyCurrentUser").asRequest())
                .flatMap(res -> res.as(User.class));
    }

    public Uni<User> getUser(Snowflake userId) {
        return requester.request(new EmptyRequest("/users/{user.id}", variables("user.id", requireNonNull(userId, "userId").getValue())))
                .flatMap(res -> res.as(User.class));
    }

    public Uni<Void> leaveGuild(Snowflake guildId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/users/@me/guilds/{guild.id}", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .replaceWithVoid();
    }

    public Uni<Channel> createDm(Snowflake recipientId) {
        return requester.request(CreateDm.create(recipientId).asRequest()).flatMap(res -> res.as(Channel.class));
    }

    public Multi<VoiceRegion> listVoiceRegions() {
        return requester.request(new EmptyRequest("/voice/regions"))
                .flatMap(res -> res.as(VoiceRegion[].class))
                .onItem().disjoint();
    }

    public Uni<Webhook> createWebhook(CreateWebhook createWebhook) {
        return requester.request(requireNonNull(createWebhook, "createWebhook").asRequest())
                .flatMap(res -> res.as(Webhook.class));
    }

    public Multi<Webhook> getChannelWebhooks(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/webhooks", variables("channel.id", requireNonNull(channelId, "channelId").getValue())))
                .flatMap(res -> res.as(Webhook[].class))
                .onItem().disjoint();
    }

    public Multi<Webhook> getGuildWebhooks(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/webhooks", variables("guild.id", requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(Webhook[].class))
                .onItem().disjoint();
    }

    public Uni<Webhook> getWebhook(Snowflake webhookId) {
        return requester.request(new EmptyRequest("/webhooks/{webhook.id}", variables("webhook.id", requireNonNull(webhookId, "webhookId").getValue())))
                .flatMap(res -> res.as(Webhook.class));
    }

    public Uni<Webhook> modifyWebhook(ModifyWebhook modifyWebhook) {
        return requester.request(requireNonNull(modifyWebhook, "modifyWebhook").asRequest())
                .flatMap(res -> res.as(Webhook.class));
    }

    public Uni<Void> deleteWebhook(Snowflake webhookId, @Nullable String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{webhook.id}", variables("webhook.id", requireNonNull(webhookId, "webhookId").getValue()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Application> getCurrentBotApplicationInformation() {
        return requester.request(new EmptyRequest("/oauth2/applications/@me")).flatMap(res -> res.as(Application.class));
    }

    public static class Builder<T extends Response> extends AuthenticatedDiscordClient.Builder<T, DiscordBotClient<T>> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        public DiscordBotClient<T> build() {
            return new DiscordBotClient<>(vertx, getRequesterFactory().apply(this), getInteractionsClientOptions());
        }
    }

    private static class BotToken implements AccessTokenSource {
        private final Uni<AccessToken> token;

        public static BotToken create(String token) {
            return new BotToken(requireNonNull(token, "token"));
        }

        private BotToken(String token) {
            this.token = Uni.createFrom().item(AccessToken.builder().tokenType(TokenType.BOT).accessToken(token).build());
        }

        @Override
        public Uni<AccessToken> getToken() {
            return token;
        }
    }
}
