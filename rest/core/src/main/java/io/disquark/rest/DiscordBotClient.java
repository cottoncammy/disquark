package io.disquark.rest;

import static io.disquark.rest.util.Variables.variables;
import static java.util.Objects.requireNonNull;

import java.time.Instant;

import javax.annotation.Nullable;

import io.disquark.rest.emoji.ReactionEmoji;
import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.application.Application;
import io.disquark.rest.json.auditlog.GetGuildAuditLogUni;
import io.disquark.rest.json.automod.AutoModerationRule;
import io.disquark.rest.json.automod.CreateAutoModerationRuleUni;
import io.disquark.rest.json.automod.ModifyAutoModerationRuleUni;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.json.channel.CreateDmUni;
import io.disquark.rest.json.channel.CreateGuildChannelUni;
import io.disquark.rest.json.channel.EditChannelPermissionsUni;
import io.disquark.rest.json.channel.FollowAnnouncementChannelUni;
import io.disquark.rest.json.channel.FollowedChannel;
import io.disquark.rest.json.channel.GroupDmAddRecipientUni;
import io.disquark.rest.json.channel.ModifyDmChannelUni;
import io.disquark.rest.json.channel.ModifyGuildChannelPositionsUni;
import io.disquark.rest.json.channel.ModifyGuildChannelUni;
import io.disquark.rest.json.emoji.CreateGuildEmojiUni;
import io.disquark.rest.json.emoji.Emoji;
import io.disquark.rest.json.emoji.ModifyGuildEmojiUni;
import io.disquark.rest.json.forum.ForumThreadMessageParams;
import io.disquark.rest.json.forum.StartThreadInForumChannelUni;
import io.disquark.rest.json.guild.BeginGuildPruneUni;
import io.disquark.rest.json.guild.CreateGuildBanUni;
import io.disquark.rest.json.guild.CreateGuildUni;
import io.disquark.rest.json.guild.GetGuildBansMulti;
import io.disquark.rest.json.guild.GetGuildPruneCountUni;
import io.disquark.rest.json.guild.Guild;
import io.disquark.rest.json.guild.GuildVanityUrl;
import io.disquark.rest.json.guild.GuildWidget;
import io.disquark.rest.json.guild.Integration;
import io.disquark.rest.json.guild.ModifyGuildMfaLevelUni;
import io.disquark.rest.json.guild.ModifyGuildUni;
import io.disquark.rest.json.guild.ModifyGuildWelcomeScreenUni;
import io.disquark.rest.json.guild.ModifyGuildWidgetUni;
import io.disquark.rest.json.guild.WelcomeScreen;
import io.disquark.rest.json.guildtemplate.CreateGuildFromGuildTemplateUni;
import io.disquark.rest.json.guildtemplate.CreateGuildTemplateUni;
import io.disquark.rest.json.guildtemplate.GuildTemplate;
import io.disquark.rest.json.guildtemplate.ModifyGuildTemplateUni;
import io.disquark.rest.json.invite.CreateChannelInviteUni;
import io.disquark.rest.json.invite.GetInviteUni;
import io.disquark.rest.json.invite.Invite;
import io.disquark.rest.json.member.AddGuildMemberUni;
import io.disquark.rest.json.member.GuildMember;
import io.disquark.rest.json.member.ListGuildMembersMulti;
import io.disquark.rest.json.member.ModifyCurrentMemberUni;
import io.disquark.rest.json.member.ModifyGuildMemberUni;
import io.disquark.rest.json.member.SearchGuildMembersMulti;
import io.disquark.rest.json.message.BulkDeleteMessagesUni;
import io.disquark.rest.json.message.CreateMessageUni;
import io.disquark.rest.json.message.EditMessageUni;
import io.disquark.rest.json.message.GetChannelMessagesMulti;
import io.disquark.rest.json.message.GetReactionsMulti;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.oauth2.AccessToken;
import io.disquark.rest.json.oauth2.TokenType;
import io.disquark.rest.json.role.CreateGuildRoleUni;
import io.disquark.rest.json.role.ModifyGuildRolePositionsMulti;
import io.disquark.rest.json.role.ModifyGuildRoleUni;
import io.disquark.rest.json.role.Role;
import io.disquark.rest.json.roleconnection.ApplicationRoleConnection;
import io.disquark.rest.json.roleconnection.UpdateApplicationRoleConnectionMetadataRecordsMulti;
import io.disquark.rest.json.scheduledevent.CreateGuildScheduledEventUni;
import io.disquark.rest.json.scheduledevent.GetGuildScheduledEventUsersMulti;
import io.disquark.rest.json.scheduledevent.GuildScheduledEvent;
import io.disquark.rest.json.scheduledevent.ModifyGuildScheduledEventUni;
import io.disquark.rest.json.stage.CreateStageInstanceUni;
import io.disquark.rest.json.stage.ModifyStageInstanceUni;
import io.disquark.rest.json.stage.StageInstance;
import io.disquark.rest.json.sticker.CreateGuildStickerUni;
import io.disquark.rest.json.sticker.ListNitroStickerPacksResponse;
import io.disquark.rest.json.sticker.ModifyGuildStickerUni;
import io.disquark.rest.json.sticker.Sticker;
import io.disquark.rest.json.sticker.StickerPack;
import io.disquark.rest.json.thread.ListThreadMembersMulti;
import io.disquark.rest.json.thread.ListThreadsResult;
import io.disquark.rest.json.thread.ListThreadsUni;
import io.disquark.rest.json.thread.ModifyThreadUni;
import io.disquark.rest.json.thread.StartThreadFromMessageUni;
import io.disquark.rest.json.thread.StartThreadWithoutMessageUni;
import io.disquark.rest.json.thread.ThreadMember;
import io.disquark.rest.json.user.ModifyCurrentUserUni;
import io.disquark.rest.json.user.User;
import io.disquark.rest.json.voice.ModifyCurrentUserVoiceStateUni;
import io.disquark.rest.json.voice.ModifyUserVoiceStateUni;
import io.disquark.rest.json.voice.VoiceRegion;
import io.disquark.rest.json.webhook.CreateWebhookUni;
import io.disquark.rest.json.webhook.ModifyWebhookUni;
import io.disquark.rest.json.webhook.Webhook;
import io.disquark.rest.request.AccessTokenSource;
import io.disquark.rest.request.EmptyRequest;
import io.disquark.rest.request.Requester;
import io.disquark.rest.response.Response;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;

public class DiscordBotClient<T extends Response> extends AuthenticatedDiscordClient<T> {

    public static <T extends Response> Builder<T> builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder<>(requireNonNull(vertx, "vertx"), requireNonNull(tokenSource, "tokenSource"));
    }

    public static <T extends Response> Builder<T> builder(Vertx vertx, String token) {
        return builder(vertx, new BotToken(token));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, AccessTokenSource tokenSource) {
        return (DiscordBotClient<T>) builder(vertx, tokenSource).build();
    }

    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, String token) {
        return create(vertx, new BotToken(token));
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

    public Multi<ApplicationRoleConnection.Metadata> getApplicationRoleConnectionMetadataRecords(Snowflake applicationId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/applications/{application.id}/role-connections/metadata",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue()))))
                .flatMap(res -> res.as(ApplicationRoleConnection.Metadata[].class))
                .onItem().disjoint();
    }

    public UpdateApplicationRoleConnectionMetadataRecordsMulti updateApplicationRoleConnectionMetadataRecords(
            Snowflake applicationId) {
        return new UpdateApplicationRoleConnectionMetadataRecordsMulti(requester, applicationId);
    }

    public GetGuildAuditLogUni getGuildAuditLog(Snowflake guildId) {
        return new GetGuildAuditLogUni(requester, guildId);
    }

    public Multi<AutoModerationRule> listAutoModerationRulesForGuild(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/auto-moderation/rules",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(AutoModerationRule[].class))
                .onItem().disjoint();
    }

    public Uni<AutoModerationRule> getAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId) {
        return deferredUni(() -> requester.request(new EmptyRequest(
                "/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "auto_moderation_rule.id", requireNonNull(autoModerationRuleId, "autoModerationRuleId").getValue()))))
                .flatMap(res -> res.as(AutoModerationRule.class));
    }

    public CreateAutoModerationRuleUni createAutoModerationRule(Snowflake guildId, String name,
            AutoModerationRule.EventType eventType, AutoModerationRule.TriggerType triggerType) {
        return new CreateAutoModerationRuleUni(requester, guildId, name, eventType, triggerType);
    }

    public ModifyAutoModerationRuleUni modifyAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId) {
        return new ModifyAutoModerationRuleUni(requester, guildId, autoModerationRuleId);
    }

    public Uni<Void> deleteAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId,
            @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "auto_moderation_rule.id", requireNonNull(autoModerationRuleId, "autoModerationRuleId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public Uni<Channel> getChannel(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/channels/{channel.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .flatMap(res -> res.as(Channel.class));
    }

    public ModifyDmChannelUni modifyDmChannel(Snowflake channelId) {
        return new ModifyDmChannelUni(requester, channelId);
    }

    public ModifyGuildChannelUni modifyGuildChannel(Snowflake channelId) {
        return new ModifyGuildChannelUni(requester, channelId);
    }

    public ModifyThreadUni modifyThread(Snowflake channelId) {
        return new ModifyThreadUni(requester, channelId);
    }

    public Uni<Channel> deleteOrCloseChannel(Snowflake channelId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()), auditLogReason)))
                .flatMap(res -> res.as(Channel.class));
    }

    public GetChannelMessagesMulti getChannelMessages(Snowflake channelId) {
        return new GetChannelMessagesMulti(requester, channelId);
    }

    public Uni<Message> getChannelMessage(Snowflake channelId, Snowflake messageId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/channels/{channel.id}/messages/{message.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue()))))
                .flatMap(res -> res.as(Message.class));
    }

    public CreateMessageUni createMessage(Snowflake channelId) {
        return new CreateMessageUni(requester, channelId);
    }

    public Uni<Message> crosspostMessage(Snowflake channelId, Snowflake messageId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.POST,
                "/channels/{channel.id}/messages/{message.id}/crosspost",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue()))))
                .flatMap(res -> res.as(Message.class));
    }

    public Uni<Void> createReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.PUT,
                "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/@me",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue(),
                        "emoji", requireNonNull(emoji, "emoji").getValue()))))
                .replaceWithVoid();
    }

    public Uni<Void> deleteOwnReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/@me",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue(),
                        "emoji", requireNonNull(emoji, "emoji").getValue()))))
                .replaceWithVoid();
    }

    public Uni<Void> deleteUserReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji, Snowflake userId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/{user.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue(),
                        "emoji", requireNonNull(emoji, "emoji").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()))))
                .replaceWithVoid();
    }

    public GetReactionsMulti getReactions(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return new GetReactionsMulti(requester, channelId, messageId, emoji);
    }

    public Uni<Void> deleteAllReactions(Snowflake channelId, Snowflake messageId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/messages/{message.id}/reactions",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue()))))
                .replaceWithVoid();
    }

    public Uni<Void> deleteAllReactionsForEmoji(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue(),
                        "emoji", requireNonNull(emoji, "emoji").getValue()))))
                .replaceWithVoid();
    }

    public EditMessageUni editMessage(Snowflake channelId, Snowflake messageId) {
        return new EditMessageUni(requester, channelId, messageId);
    }

    public Uni<Void> deleteMessage(Snowflake channelId, Snowflake messageId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/messages/{message.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public BulkDeleteMessagesUni bulkDeleteMessages(Snowflake channelId) {
        return new BulkDeleteMessagesUni(requester, channelId);
    }

    public EditChannelPermissionsUni editChannelPermissions(Snowflake channelId, Snowflake overwriteId,
            Channel.Overwrite.Type type) {
        return new EditChannelPermissionsUni(requester, channelId, overwriteId, type);
    }

    public Multi<Invite> getChannelInvites(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/channels/{channel.id}/invites",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .flatMap(res -> res.as(Invite[].class))
                .onItem().disjoint();
    }

    public CreateChannelInviteUni createChannelInvite(Snowflake channelId) {
        return new CreateChannelInviteUni(requester, channelId);
    }

    public Uni<Void> deleteChannelPermission(Snowflake channelId, Snowflake overwriteId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/permissions/{overwrite.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "overwrite.id", requireNonNull(overwriteId, "overwriteId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public Uni<FollowedChannel> followAnnouncementChannel(Snowflake channelId, Snowflake webhookChannelId) {
        return deferredUni(() -> new FollowAnnouncementChannelUni(requester, channelId, webhookChannelId));
    }

    public Uni<Void> triggerTypingIndicator(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.POST, "/channels/{channel.id}/typing",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .replaceWithVoid();
    }

    public Multi<Message> getPinnedMessages(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/channels/{channel.id}/pins",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .flatMap(res -> res.as(Message[].class))
                .onItem().disjoint();
    }

    public Uni<Void> pinMessage(Snowflake channelId, Snowflake messageId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/pins/{message.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public Uni<Void> unpinMessage(Snowflake channelId, Snowflake messageId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/pins/{message.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "message.id", requireNonNull(messageId, "messageId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public GroupDmAddRecipientUni groupDmAddRecipient(Snowflake channelId, Snowflake userId, String accessToken) {
        return new GroupDmAddRecipientUni(requester, channelId, userId, accessToken);
    }

    public Uni<Void> groupDmRemoveRecipient(Snowflake channelId, Snowflake userId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/recipients/users/{user.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()))))
                .replaceWithVoid();
    }

    public StartThreadFromMessageUni startThreadFromMessage(Snowflake channelId, Snowflake messageId, String name) {
        return new StartThreadFromMessageUni(requester, channelId, messageId, name);
    }

    public StartThreadWithoutMessageUni startThreadWithoutMessage(Snowflake channelId, String name) {
        return new StartThreadWithoutMessageUni(requester, channelId, name);
    }

    public StartThreadInForumChannelUni startThreadInForumChannel(Snowflake channelId, String name,
            ForumThreadMessageParams message) {
        return new StartThreadInForumChannelUni(requester, channelId, name, message);
    }

    public Uni<Void> joinThread(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/thread-members/@me",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .replaceWithVoid();
    }

    public Uni<Void> addThreadMember(Snowflake channelId, Snowflake userId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.PUT,
                "/channels/{channel.id}/thread-members/{user.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()))))
                .replaceWithVoid();
    }

    public Uni<Void> leaveThread(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/thread-members/@me",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .replaceWithVoid();
    }

    public Uni<Void> removeThreadMember(Snowflake channelId, Snowflake userId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/channels/{channel.id}/thread-members/{user.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()))))
                .replaceWithVoid();
    }

    public Uni<ThreadMember> getThreadMember(Snowflake channelId, Snowflake userId, boolean withMember) {
        return deferredUni(() -> requester.request(new EmptyRequest(
                "/channels/{channel.id}/thread-members/{user.id}{?with_member}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue(),
                        "with_member", withMember))))
                .flatMap(res -> res.as(ThreadMember.class));
    }

    public ListThreadMembersMulti listThreadMembers(Snowflake channelId) {
        return new ListThreadMembersMulti(requester, channelId);
    }

    public ListThreadsUni listPublicArchivedThreads(Snowflake channelId) {
        return new ListThreadsUni(requester,
                "/channels/{channel.id}/threads/archived/public{?before,limit}", channelId);
    }

    public ListThreadsUni listPrivateArchivedThreads(Snowflake channelId) {
        return new ListThreadsUni(requester,
                "/channels/{channel.id}/threads/archived/private{?before,limit}", channelId);
    }

    public ListThreadsUni listJoinedPrivateArchivedThreads(Snowflake channelId) {
        return new ListThreadsUni(requester,
                "/channels/{channel.id}/users/@me/threads/archived/private{?before,limit}", channelId);
    }

    public Multi<Emoji> listGuildEmojis(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/emojis",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Emoji[].class))
                .onItem().disjoint();
    }

    public Uni<Emoji> getGuildEmoji(Snowflake guildId, Snowflake emojiId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/emojis/{emoji.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "emoji.id", requireNonNull(emojiId, "emojiId").getValue()))))
                .flatMap(res -> res.as(Emoji.class));
    }

    public CreateGuildEmojiUni createGuildEmoji(Snowflake guildId, String name, Buffer image) {
        return new CreateGuildEmojiUni(requester, guildId, name, image);
    }

    public ModifyGuildEmojiUni modifyGuildEmoji(Snowflake guildId, Snowflake emojiId) {
        return new ModifyGuildEmojiUni(requester, guildId, emojiId);
    }

    public Uni<Void> deleteGuildEmoji(Snowflake guildId, Snowflake emojiId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/emojis/{emoji.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "emoji.id", requireNonNull(emojiId, "emojiId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public CreateGuildUni createGuild(String name) {
        return new CreateGuildUni(requester, name);
    }

    public Uni<Guild> getGuild(Snowflake guildId, boolean withCounts) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}{?with_counts}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "with_counts", withCounts))))
                .flatMap(res -> res.as(Guild.class));
    }

    public Uni<Guild.Preview> getGuildPreview(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/preview",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Guild.Preview.class));
    }

    public ModifyGuildUni modifyGuild(Snowflake guildId) {
        return new ModifyGuildUni(requester, guildId);
    }

    public Uni<Void> deleteGuild(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .replaceWithVoid();
    }

    public Multi<Channel> getGuildChannels(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/channels",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Channel[].class))
                .onItem().disjoint();
    }

    public CreateGuildChannelUni createGuildChannel(Snowflake guildId, String name) {
        return new CreateGuildChannelUni(requester, guildId, name);
    }

    public ModifyGuildChannelPositionsUni modifyGuildChannelPositions(Snowflake guildId) {
        return new ModifyGuildChannelPositionsUni(requester, guildId);
    }

    public Uni<ListThreadsResult> listActiveGuildThreads(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/threads/active",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(ListThreadsResult.class));
    }

    public Uni<GuildMember> getGuildMember(Snowflake guildId, Snowflake userId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/members/{user.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()))))
                .flatMap(res -> res.as(GuildMember.class));
    }

    public ListGuildMembersMulti listGuildMembers(Snowflake guildId) {
        return new ListGuildMembersMulti(requester, guildId);
    }

    public SearchGuildMembersMulti searchGuildMembers(Snowflake guildId, String query) {
        return new SearchGuildMembersMulti(requester, guildId, query);
    }

    public AddGuildMemberUni addGuildMember(Snowflake guildId, Snowflake userId, String accessToken) {
        return new AddGuildMemberUni(requester, guildId, userId, accessToken);
    }

    public ModifyGuildMemberUni modifyGuildMember(Snowflake guildId, Snowflake userId) {
        return new ModifyGuildMemberUni(requester, guildId, userId);
    }

    public ModifyCurrentMemberUni modifyCurrentMember(Snowflake guildId) {
        return new ModifyCurrentMemberUni(requester, guildId);
    }

    public Uni<Void> addGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId,
            @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.PUT,
                "/guilds/{guild.id}/members/{user.id}/roles/{role.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "user.id",
                        requireNonNull(userId, "userId").getValue(), "role.id",
                        requireNonNull(roleId, "roleId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public Uni<Void> removeGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId,
            @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/guilds/{guild.id}/members/{user.id}/roles/{role.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue(),
                        "role.id", requireNonNull(roleId, "roleId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public Uni<Void> removeGuildMember(Snowflake guildId, Snowflake userId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/members/{user.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public GetGuildBansMulti getGuildBans(Snowflake guildId) {
        return new GetGuildBansMulti(requester, guildId);
    }

    public Uni<Guild.Ban> getGuildBan(Snowflake guildId, Snowflake userId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/bans/{user.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()))))
                .flatMap(res -> res.as(Guild.Ban.class));
    }

    public CreateGuildBanUni createGuildBan(Snowflake guildId, Snowflake userId) {
        return new CreateGuildBanUni(requester, guildId, userId);
    }

    public Uni<Void> removeGuildBan(Snowflake guildId, Snowflake userId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/bans/{user.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "user.id", requireNonNull(userId, "userId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public Multi<Role> getGuildRoles(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/roles",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Role[].class))
                .onItem().disjoint();
    }

    public CreateGuildRoleUni createGuildRole(Snowflake guildId) {
        return new CreateGuildRoleUni(requester, guildId);
    }

    public ModifyGuildRolePositionsMulti modifyGuildRolePositions(Snowflake guildId) {
        return new ModifyGuildRolePositionsMulti(requester, guildId);
    }

    public ModifyGuildRoleUni modifyGuildRole(Snowflake guildId, Snowflake roleId) {
        return new ModifyGuildRoleUni(requester, guildId, roleId);
    }

    public ModifyGuildMfaLevelUni modifyGuildMfaLevel(Snowflake guildId, Guild.MfaLevel level) {
        return new ModifyGuildMfaLevelUni(requester, guildId, level);
    }

    public Uni<Void> deleteGuildRole(Snowflake guildId, Snowflake roleId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/roles/{role.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "role.id", requireNonNull(roleId, "roleId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public GetGuildPruneCountUni getGuildPruneCount(Snowflake guildId) {
        return new GetGuildPruneCountUni(requester, guildId);
    }

    public BeginGuildPruneUni beginGuildPrune(Snowflake guildId) {
        return new BeginGuildPruneUni(requester, guildId);
    }

    public Multi<VoiceRegion> getGuildVoiceRegions(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/regions",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(VoiceRegion[].class))
                .onItem().disjoint();
    }

    public Multi<Invite> getGuildInvites(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/invites",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Invite[].class))
                .onItem().disjoint();
    }

    public Multi<Integration> getGuildIntegrations(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/integrations",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Integration[].class))
                .onItem().disjoint();
    }

    public Uni<Void> deleteGuildIntegration(Snowflake guildId, Snowflake integrationId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/guilds/{guild.id}/integrations/{integration.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "integration.id", requireNonNull(integrationId, "integrationId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public Uni<Guild.WidgetSettings> getGuildWidgetSettings(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/widget",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Guild.WidgetSettings.class));
    }

    public ModifyGuildWidgetUni modifyGuildWidget(Snowflake guildId) {
        return new ModifyGuildWidgetUni(requester, guildId);
    }

    public Uni<GuildWidget> getGuildWidget(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/widget.json",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(GuildWidget.class));
    }

    public Uni<GuildVanityUrl> getGuildVanityUrl(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/vanity-url",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(GuildVanityUrl.class));
    }

    public Uni<WelcomeScreen> getGuildWelcomeScreen(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/welcome-screen",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(WelcomeScreen.class));
    }

    public ModifyGuildWelcomeScreenUni modifyGuildWelcomeScreen(Snowflake guildId) {
        return new ModifyGuildWelcomeScreenUni(requester, guildId);
    }

    public ModifyCurrentUserVoiceStateUni modifyCurrentUserVoiceState(Snowflake guildId) {
        return new ModifyCurrentUserVoiceStateUni(requester, guildId);
    }

    public ModifyUserVoiceStateUni modifyUserVoiceState(Snowflake guildId, Snowflake userId, Snowflake channelId) {
        return new ModifyUserVoiceStateUni(requester, guildId, userId, channelId);
    }

    public Multi<GuildScheduledEvent> listScheduledEventsForGuild(Snowflake guildId, boolean withUserCount) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/scheduled-events{?with_user_count}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(), "with_user_count", withUserCount))))
                .flatMap(res -> res.as(GuildScheduledEvent[].class))
                .onItem().disjoint();
    }

    public CreateGuildScheduledEventUni createGuildScheduledEvent(Snowflake guildId, String name,
            GuildScheduledEvent.PrivacyLevel privacyLevel, Instant scheduledStartTime,
            GuildScheduledEvent.EntityType entityType) {
        return new CreateGuildScheduledEventUni(requester, guildId, name, privacyLevel, scheduledStartTime, entityType);
    }

    public Uni<GuildScheduledEvent> getGuildScheduledEvent(Snowflake guildId, Snowflake guildScheduledEventId,
            boolean withUserCount) {
        return deferredUni(() -> requester.request(new EmptyRequest(
                "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}{?with_user_count}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "guild_scheduled_event.id", requireNonNull(guildScheduledEventId, "guildScheduledEventId").getValue(),
                        "with_user_count", withUserCount))))
                .flatMap(res -> res.as(GuildScheduledEvent.class));
    }

    public ModifyGuildScheduledEventUni modifyGuildScheduledEvent(Snowflake guildId, Snowflake guildScheduledEventId) {
        return new ModifyGuildScheduledEventUni(requester, guildId, guildScheduledEventId);
    }

    public Uni<Void> deleteGuildScheduledEvent(Snowflake guildId, Snowflake guildScheduledEventId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "guild_scheduled_event.id",
                        requireNonNull(guildScheduledEventId, "guildScheduledEventId").getValue()))))
                .replaceWithVoid();
    }

    public GetGuildScheduledEventUsersMulti getGuildScheduledEventUsers(Snowflake guildId, Snowflake guildScheduledEventId) {
        return new GetGuildScheduledEventUsersMulti(requester, guildId, guildScheduledEventId);
    }

    public Uni<GuildTemplate> getGuildTemplate(String templateCode) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/templates/{template.code}",
                variables("template.code", requireNonNull(templateCode, "templateCode")))))
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public CreateGuildFromGuildTemplateUni createGuildFromGuildTemplate(String templateCode, String name) {
        return new CreateGuildFromGuildTemplateUni(requester, templateCode, name);
    }

    public Multi<GuildTemplate> getGuildTemplates(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/templates",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(GuildTemplate[].class))
                .onItem().disjoint();
    }

    public CreateGuildTemplateUni createGuildTemplate(Snowflake guildId, String name) {
        return new CreateGuildTemplateUni(requester, guildId, name);
    }

    public Uni<GuildTemplate> syncGuildTemplate(Snowflake guildId, String templateCode) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.PUT,
                "/guilds/{guild.id}/templates/{template.code}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "template.code", requireNonNull(templateCode, "templateCode")))))
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public ModifyGuildTemplateUni modifyGuildTemplate(Snowflake guildId, String templateCode) {
        return new ModifyGuildTemplateUni(requester, guildId, templateCode);
    }

    public Uni<Void> deleteGuildTemplate(Snowflake guildId, String templateCode) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/guilds/{guild.id}/templates/{template.code}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "template.code", requireNonNull(templateCode, "templateCode")))))
                .replaceWithVoid();
    }

    public GetInviteUni getInvite(String inviteCode) {
        return new GetInviteUni(requester, inviteCode);
    }

    public Uni<Invite> deleteInvite(String inviteCode, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/invites/{invite.code}",
                variables("invite.code", requireNonNull(inviteCode, "inviteCode")), auditLogReason)))
                .flatMap(res -> res.as(Invite.class));
    }

    public CreateStageInstanceUni createStageInstance(Snowflake channelId, String topic) {
        return new CreateStageInstanceUni(requester, channelId, topic);
    }

    public Uni<StageInstance> getStageInstance(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/stage-instances/{channel.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .flatMap(res -> res.as(StageInstance.class));
    }

    public ModifyStageInstanceUni modifyStageInstance(Snowflake channelId) {
        return new ModifyStageInstanceUni(requester, channelId);
    }

    public Uni<Void> deleteStageInstance(Snowflake channelId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/stage-instances/{channel.id}",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()), auditLogReason)))
                .replaceWithVoid();
    }

    public Uni<Sticker> getSticker(Snowflake stickerId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/stickers/{sticker.id}",
                variables("sticker.id", requireNonNull(stickerId, "stickerId").getValue()))))
                .flatMap(res -> res.as(Sticker.class));
    }

    public Multi<StickerPack> listNitroStickerPacks() {
        return requester.request(new EmptyRequest("/sticker-packs"))
                .flatMap(res -> res.as(ListNitroStickerPacksResponse.class))
                .map(ListNitroStickerPacksResponse::getStickerPacks)
                .onItem().disjoint();
    }

    public Multi<Sticker> listGuildStickers(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/stickers",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Sticker[].class))
                .onItem().disjoint();
    }

    public Uni<Sticker> getGuildSticker(Snowflake guildId, Snowflake stickerId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/stickers/{sticker.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "sticker.id", requireNonNull(stickerId, "stickerId").getValue()))))
                .flatMap(res -> res.as(Sticker.class));
    }

    public CreateGuildStickerUni createGuildSticker(Snowflake guildId, String name, String description, String tags) {
        return new CreateGuildStickerUni(requester, guildId, name, description, tags);
    }

    public ModifyGuildStickerUni modifyGuildSticker(Snowflake guildId, Snowflake stickerId) {
        return new ModifyGuildStickerUni(requester, guildId, stickerId);
    }

    public Uni<Void> deleteGuildSticker(Snowflake guildId, Snowflake stickerId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/guilds/{guild.id}/stickers/{sticker.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "sticker.id", requireNonNull(stickerId, "stickerId").getValue()),
                auditLogReason)))
                .replaceWithVoid();
    }

    public ModifyCurrentUserUni modifyCurrentUser() {
        return new ModifyCurrentUserUni(requester);
    }

    public Uni<User> getUser(Snowflake userId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/users/{user.id}",
                variables("user.id", requireNonNull(userId, "userId").getValue()))))
                .flatMap(res -> res.as(User.class));
    }

    public Uni<Void> leaveGuild(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/users/@me/guilds/{guild.id}",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .replaceWithVoid();
    }

    public Uni<Channel> createDm(Snowflake recipientId) {
        return deferredUni(() -> new CreateDmUni(requester, recipientId));
    }

    public Multi<VoiceRegion> listVoiceRegions() {
        return requester.request(new EmptyRequest("/voice/regions"))
                .flatMap(res -> res.as(VoiceRegion[].class))
                .onItem().disjoint();
    }

    public CreateWebhookUni createWebhook(Snowflake channelId, String name) {
        return new CreateWebhookUni(requester, channelId, name);
    }

    public Multi<Webhook> getChannelWebhooks(Snowflake channelId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/channels/{channel.id}/webhooks",
                variables("channel.id", requireNonNull(channelId, "channelId").getValue()))))
                .flatMap(res -> res.as(Webhook[].class))
                .onItem().disjoint();
    }

    public Multi<Webhook> getGuildWebhooks(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/guilds/{guild.id}/webhooks",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Webhook[].class))
                .onItem().disjoint();
    }

    public Uni<Webhook> getWebhook(Snowflake webhookId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/webhooks/{webhook.id}",
                variables("webhook.id", requireNonNull(webhookId, "webhookId").getValue()))))
                .flatMap(res -> res.as(Webhook.class));
    }

    public ModifyWebhookUni modifyWebhook(Snowflake webhookId) {
        return new ModifyWebhookUni(requester, webhookId);
    }

    public Uni<Void> deleteWebhook(Snowflake webhookId, @Nullable String auditLogReason) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{webhook.id}",
                variables("webhook.id", requireNonNull(webhookId, "webhookId").getValue()), auditLogReason)))
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

        protected BotToken(String token) {
            this.token = Uni.createFrom().item(new AccessToken(TokenType.BOT, token));
        }

        @Override
        public Uni<AccessToken> getToken() {
            return token;
        }
    }
}
