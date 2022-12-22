package org.example.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.request.*;
import org.example.rest.resources.*;
import org.example.rest.resources.application.ApplicationRoleConnectionMetadata;
import org.example.rest.resources.auditlog.AuditLog;
import org.example.rest.resources.auditlog.GetGuildAuditLog;
import org.example.rest.resources.automod.AutoModerationRule;
import org.example.rest.resources.automod.CreateAutoModerationRule;
import org.example.rest.resources.automod.ModifyAutoModerationRule;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.channel.CreateChannelInvite;
import org.example.rest.resources.channel.EditChannelPermissions;
import org.example.rest.resources.channel.FollowedChannel;
import org.example.rest.resources.channel.forum.StartThreadInForumChannel;
import org.example.rest.resources.channel.message.EditMessage;
import org.example.rest.resources.channel.thread.StartThreadFromMessage;
import org.example.rest.resources.channel.thread.StartThreadWithoutMessage;
import org.example.rest.resources.channel.thread.ThreadMember;
import org.example.rest.resources.channel.message.CreateMessage;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.emoji.CreateGuildEmoji;
import org.example.rest.resources.emoji.Emoji;
import org.example.rest.resources.emoji.ModifyGuildEmoji;
import org.example.rest.resources.guild.*;
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
import org.example.rest.resources.permissions.Role;
import org.example.rest.resources.stageinstance.CreateStageInstance;
import org.example.rest.resources.stageinstance.ModifyStageInstance;
import org.example.rest.resources.stageinstance.StageInstance;
import org.example.rest.resources.sticker.CreateGuildSticker;
import org.example.rest.resources.sticker.ModifyGuildSticker;
import org.example.rest.resources.sticker.Sticker;
import org.example.rest.resources.user.User;
import org.example.rest.resources.voice.VoiceRegion;
import org.example.rest.resources.webhook.CreateWebhook;
import org.example.rest.resources.webhook.ModifyWebhook;
import org.example.rest.resources.webhook.Webhook;
import org.example.rest.response.Response;
import org.example.rest.util.ReactionEmoji;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class DiscordBotClient<T extends Response> extends DiscordClient<T> {

    public static <T extends Response> Builder<T> builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder<>(requireNonNull(vertx), requireNonNull(tokenSource));
    }

    public static <T extends Response> Builder<T> builder(Vertx vertx, String token) {
        return builder(vertx, BotToken.create(requireNonNull(token)));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, AccessTokenSource tokenSource) {
        return (DiscordBotClient<T>) builder(vertx, tokenSource).build();
    }

    public static <T extends Response> DiscordBotClient<T> create(Vertx vertx, String token) {
        return create(vertx, BotToken.create(token));
    }

    private DiscordBotClient(Vertx vertx, Requester<T> requester) {
        super(vertx, requester);
    }

    public Multi<ApplicationRoleConnectionMetadata> getApplicationRoleConnectionMetadataRecords(Snowflake applicationId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/role-connections/metadata", Variables.variables().set("application.id", applicationId.getValueAsString())))
                .flatMap(res -> res.as(ApplicationRoleConnectionMetadata[].class))
                .onItem().disjoint();
    }

    public Multi<ApplicationRoleConnectionMetadata> updateApplicationRoleConnectionMetadataRecords() {

    }

    public Uni<AuditLog> getGuildAuditLog(GetGuildAuditLog getGuildAuditLog) {
        return requester.request(getGuildAuditLog.asRequest()).flatMap(res -> res.as(AuditLog.class));
    }

    public Multi<AutoModerationRule> listAutoModerationRulesForGuild(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/auto-moderation/rules", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(AutoModerationRule[].class))
                .onItem().disjoint();
    }

    public Uni<AutoModerationRule> getAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("auto_moderation_rule.id", autoModerationRuleId.getValueAsString())))
                .flatMap(res -> res.as(AutoModerationRule.class));
    }

    public Uni<AutoModerationRule> createAutoModerationRule(CreateAutoModerationRule createAutoModerationRule) {
        return requester.request(createAutoModerationRule.asRequest()).flatMap(res -> res.as(AutoModerationRule.class));
    }

    public Uni<AutoModerationRule> modifyAutoModerationRule(ModifyAutoModerationRule modifyAutoModerationRule) {
        return requester.request(modifyAutoModerationRule.asRequest()).flatMap(res -> res.as(AutoModerationRule.class));
    }

    public Uni<Void> deleteAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("auto_moderation_rule.id", autoModerationRuleId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteAutoModerationRule(Snowflake guildId, Snowflake autoModerationRuleId) {
        return deleteAutoModerationRule(guildId, autoModerationRuleId, null);
    }

    public Uni<Channel> getChannel(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> modifyChannel() {

    }

    public Uni<Channel> deleteOrCloseChannel(Snowflake channelId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}", Variables.variables().set("channel.id", channelId.getValueAsString()), auditLogReason))
                .flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> deleteOrCloseChannel(Snowflake channelId) {
        return deleteOrCloseChannel(channelId, null);
    }

    public Multi<Message> getChannelMessages() {

    }

    public Uni<Message> getChannelMessage(Snowflake channelId, Snowflake messageId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/messages/{message.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString())))
                .flatMap(res -> res.as(Message.class));
    }

    public Uni<Message> createMessage(CreateMessage createMessage) {
        return requester.request(createMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    public Uni<Message> crosspostMessage(Snowflake channelId, Snowflake messageId) {
        return requester.request(new EmptyRequest(HttpMethod.POST, "/channels/{channel.id}/messages/{message.id}/crosspost", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString())))
                .flatMap(res -> res.as(Message.class));
    }

    public Uni<Void> createReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/@me", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString()).set("emoji", emoji.getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> deleteOwnReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/@me", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString()).set("emoji", emoji.getValue())))
                .replaceWithVoid();
    }

    public Uni<Void> deleteUserReaction(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}/{user.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString()).set("emoji", emoji.getValue()).set("user.id", userId.getValueAsString())))
                .replaceWithVoid();
    }

    public Multi<User> getReactions() {

    }

    public Uni<Void> deleteAllReactions(Snowflake channelId, Snowflake messageId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString())))
                .replaceWithVoid();
    }

    public Uni<Void> deleteAllReactionsForEmoji(Snowflake channelId, Snowflake messageId, ReactionEmoji emoji) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}/reactions/{emoji}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString()).set("emoji", emoji.getValue())))
                .replaceWithVoid();
    }

    public Uni<Message> editMessage(EditMessage editMessage) {
        return requester.request(editMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    public Uni<Void> deleteMessage(Snowflake channelId, Snowflake messageId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/messages/{message.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteMessage(Snowflake channelId, Snowflake messageId) {
        return deleteMessage(channelId, messageId, null);
    }

    public Uni<Void> bulkDeleteMessages(List<Snowflake> messages, String auditLogReason) {

    }

    public Uni<Void> bulkDeleteMessages(List<Snowflake> messages) {
        return bulkDeleteMessages(messages, null);
    }

    public Uni<Void> editChannelPermissions(EditChannelPermissions editChannelPermissions) {
        return requester.request(editChannelPermissions.asRequest()).replaceWithVoid();
    }

    public Multi<Invite> getChannelInvites(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/invites", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .flatMap(res -> res.as(Invite[].class))
                .onItem().disjoint();
    }

    public Uni<Invite> createChannelInvite(CreateChannelInvite createChannelInvite) {
        return requester.request(createChannelInvite.asRequest()).flatMap(res -> res.as(Invite.class));
    }

    public Uni<Void> deleteChannelPermission(Snowflake channelId, Snowflake overwriteId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/permissions/{overwrite.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("overwrite.id", overwriteId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteChannelPermission(Snowflake channelId, Snowflake overwriteId) {
        return deleteChannelPermission(channelId, overwriteId, null);
    }

    public Uni<FollowedChannel> followAnnouncementChannel(Snowflake webhookChannelId) {

    }

    public Uni<Void> triggerTypingIndicator(Snowflake channelId) {
        return requester.request(new EmptyRequest(HttpMethod.POST, "/channels/{channel.id}/typing", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .replaceWithVoid();
    }

    public Multi<Message> getPinnedMessages(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/pins", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .flatMap(res -> res.as(Message[].class))
                .onItem().disjoint();
    }

    public Uni<Void> pinMessage(Snowflake channelId, Snowflake messageId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/pins/{message.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> pinMessage(Snowflake channelId, Snowflake messageId) {
        return pinMessage(channelId, messageId, null);
    }

    public Uni<Void> unpinMessage(Snowflake channelId, Snowflake messageId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/pins/{message.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("message.id", messageId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> unpinMessage(Snowflake channelId, Snowflake messageId) {
        return unpinMessage(channelId, messageId, null);
    }

    public Uni<Void> groupDmAddRecipient(Snowflake channelId, Snowflake userId, String accessToken, String nick) {

    }

    public Uni<Void> groupDmRemoveRecipient(Snowflake channelId, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/recipients/users/{user.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("user.id", userId.getValueAsString())))
                .replaceWithVoid();
    }

    public Uni<Channel> startThreadFromMessage(StartThreadFromMessage startThreadFromMessage) {
        return requester.request(startThreadFromMessage.asRequest()).flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> startThreadWithoutMessage(StartThreadWithoutMessage startThreadWithoutMessage) {
        return requester.request(startThreadWithoutMessage.asRequest()).flatMap(res -> res.as(Channel.class));
    }

    public Uni<Channel> startThreadInForumChannel(StartThreadInForumChannel startThreadInForumChannel) {
        return requester.request(startThreadInForumChannel.asRequest()).flatMap(res -> res.as(Channel.class));
    }

    public Uni<Void> joinThread(Snowflake channelId) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/thread-members/@me", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .replaceWithVoid();
    }

    public Uni<Void> addThreadMember(Snowflake channelId, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/channels/{channel.id}/thread-members/{user.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("user.id", userId.getValueAsString())))
                .replaceWithVoid();
    }

    public Uni<Void> leaveThread(Snowflake channelId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/thread-members/@me", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .replaceWithVoid();
    }

    public Uni<Void> removeThreadMember(Snowflake channelId, Snowflake userId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/channels/{channel.id}/thread-members/{user.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("user.id", userId.getValueAsString())))
                .replaceWithVoid();
    }

    public Uni<ThreadMember> getThreadMember(Snowflake channelId, Snowflake userId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/thread-members/{user.id}", Variables.variables().set("channel.id", channelId.getValueAsString()).set("user.id", userId.getValueAsString())))
                .flatMap(res -> res.as(ThreadMember.class));
    }

    public Multi<ThreadMember> listThreadMembers(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/thread-members/{user.id}", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .flatMap(res -> res.as(ThreadMember[].class))
                .onItem().disjoint();
    }

    public Multi<Object> listPublicArchivedThreads() {

    }

    public Multi<Object> listPrivateArchivedThreads() {

    }

    public Multi<Object> listJoinedPrivateArchivedThreads() {

    }

    public Multi<Emoji> listGuildEmojis(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/emojis", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Emoji[].class))
                .onItem().disjoint();
    }

    public Uni<Emoji> getGuildEmoji(Snowflake guildId, Snowflake emojiId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/emojis/{emoji.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("emoji.id", emojiId.getValueAsString())))
                .flatMap(res -> res.as(Emoji.class));
    }

    public Uni<Emoji> createGuildEmoji(CreateGuildEmoji createGuildEmoji) {
        return requester.request(createGuildEmoji.asRequest()).flatMap(res -> res.as(Emoji.class));
    }

    public Uni<Emoji> modifyGuildEmoji(ModifyGuildEmoji modifyGuildEmoji) {
        return requester.request(modifyGuildEmoji.asRequest()).flatMap(res -> res.as(Emoji.class));
    }

    public Uni<Void> deleteGuildEmoji(Snowflake guildId, Snowflake emojiId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/emojis/{emoji.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("emoji.id", emojiId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteGuildEmoji(Snowflake guildId, Snowflake emojiId) {
        return deleteGuildEmoji(guildId, emojiId, null);
    }

    public Uni<Guild> createGuild(CreateGuild createGuild) {
        return requester.request(createGuild.asRequest()).flatMap(res -> res.as(Guild.class));
    }

    public Uni<Guild> getGuild(Snowflake guildId, boolean withCounts) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}{?with_counts}", Variables.variables(JsonObject.of("guild.id", guildId.getValue(), "with_counts", withCounts))))
                .flatMap(res -> res.as(Guild.class));
    }

    public Uni<Guild.Preview> getGuildPreview(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/preview", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Guild.Preview.class));
    }

    public Uni<Guild> modifyGuild(ModifyGuild modifyGuild) {
        return requester.request(modifyGuild.asRequest()).flatMap(res -> res.as(Guild.class));
    }

    public Uni<Void> deleteGuild(Snowflake guildId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .replaceWithVoid();
    }

    public Multi<Channel> getGuildChannels(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/channels", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Channel[].class))
                .onItem().disjoint();
    }

    public Uni<Channel> createGuildChannel(CreateGuildChannel createGuildChannel) {
        return requester.request(createGuildChannel.asRequest()).flatMap(res -> res.as(Channel.class));
    }

    public Uni<Void> modifyGuildChannelPositions() {

    }

    public Multi<Object> listActiveGuildThreads() {

    }

    public Uni<Guild.Member> getGuildMember(Snowflake guildId, Snowflake userId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/members/{user.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("user.id", userId.getValueAsString())))
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Multi<Guild.Member> listGuildMembers() {

    }

    public Multi<Guild.Member> searchGuildMembers() {

    }

    public Uni<Guild.Member> addGuildMember(AddGuildMember addGuildMember) {
        return requester.request(addGuildMember.asRequest()).flatMap(res -> res.as(Guild.Member.class));
    }

    public Uni<Guild.Member> modifyGuildMember(ModifyGuildMember modifyGuildMember) {
        return requester.request(modifyGuildMember.asRequest()).flatMap(res -> res.as(Guild.Member.class));
    }

    public Uni<Void> addGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/guilds/{guild.id}/members/{user.id}/roles/{role.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("user.id", userId.getValueAsString()).set("role.id", roleId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> addGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId) {
        return addGuildMemberRole(guildId, userId, roleId, null);
    }

    public Uni<Void> removeGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/members/{user.id}/roles/{role.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("user.id", userId.getValueAsString()).set("role.id", roleId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> removeGuildMemberRole(Snowflake guildId, Snowflake userId, Snowflake roleId) {
        return removeGuildMemberRole(guildId, userId, roleId, null);
    }

    public Uni<Void> removeGuildMember(Snowflake guildId, Snowflake userId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/members/{user.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("user.id", userId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> removeGuildMember(Snowflake guildId, Snowflake userId) {
        return removeGuildMember(guildId, userId, null);
    }

    public Multi<Guild.Ban> getGuildBans() {

    }

    public Uni<Guild.Ban> getGuildBan(Snowflake guildId, Snowflake userId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/bans/{user.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("user.id", userId.getValueAsString())))
                .flatMap(res -> res.as(Guild.Ban.class));
    }

    public Uni<Void> createGuildBan(CreateGuildBan createGuildBan) {
        return requester.request(createGuildBan.asRequest()).replaceWithVoid();
    }

    public Uni<Void> removeGuildBan(Snowflake guildId, Snowflake userId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/bans/{user.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("user.id", userId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> removeGuildBan(Snowflake guildId, Snowflake userId) {
        return removeGuildBan(guildId, userId, null);
    }

    public Multi<Role> getGuildRoles(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/roles", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Role[].class))
                .onItem().disjoint();
    }

    public Uni<Role> createGuildRole(CreateGuildRole createGuildRole) {
        return requester.request(createGuildRole.asRequest()).flatMap(res -> res.as(Role.class));
    }

    public Multi<Role> modifyGuildRolePositions() {

    }

    public Uni<Role> modifyGuildRole(ModifyGuildRole modifyGuildRole) {
        return requester.request(modifyGuildRole.asRequest()).flatMap(res -> res.as(Role.class));
    }

    public Uni<Guild.MfaLevel> modifyGuildMfaLevel(Guild.MfaLevel level) {

    }

    public Uni<Void> deleteGuildRole(Snowflake guildId, Snowflake roleId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/roles/{role.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("role.id", roleId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteGuildRole(Snowflake guildId, Snowflake roleId) {
        return deleteGuildRole(guildId, roleId, null);
    }

    public Uni<Object> getGuildPruneCount() {

    }

    public Uni<Object> beginGuildPrune() {

    }

    public Multi<VoiceRegion> getGuildVoiceRegions(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/regions", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(VoiceRegion[].class))
                .onItem().disjoint();
    }

    public Multi<Invite> getGuildInvites(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/invites", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Invite[].class))
                .onItem().disjoint();
    }

    public Multi<Guild.Integration> getGuildIntegrations(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/integrations", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Guild.Integration[].class))
                .onItem().disjoint();
    }

    public Uni<Void> deleteGuildIntegration(Snowflake guildId, Snowflake integrationId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/integrations/{integration.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("integration.id", integrationId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteGuildIntegration(Snowflake guildId, Snowflake integrationId) {
        return deleteGuildIntegration(guildId, integrationId, null);
    }

    public Uni<Guild.WidgetSettings> getGuildWidgetSettings(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/widget", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Guild.WidgetSettings.class));
    }

    public Uni<Guild.Widget> modifyGuildWidget(ModifyGuildWidget modifyGuildWidget) {
        return requester.request(modifyGuildWidget.asRequest()).flatMap(res -> res.as(Guild.Widget.class));
    }

    public Uni<Guild.Widget> getGuildWidget(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/widget.json", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Guild.Widget.class));
    }

    public Uni<Invite> getGuildVanityUrl(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/vanity-url", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Invite.class));
    }

    public Uni<Guild.WelcomeScreen> getGuildWelcomeScreen(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/welcome-screen", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Guild.WelcomeScreen.class));
    }

    public Uni<Guild.WelcomeScreen> modifyGuildWelcomeScreen(ModifyGuildWelcomeScreen modifyGuildWelcomeScreen) {
        return requester.request(modifyGuildWelcomeScreen.asRequest()).flatMap(res -> res.as(Guild.WelcomeScreen.class));
    }

    public Uni<Void> modifyUserVoiceState(Snowflake guildId, Snowflake userId, Snowflake channelId, boolean suppress) {

    }

    public Multi<GuildScheduledEvent> listScheduledEventsForGuild(Snowflake guildId, boolean withUserCount) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/scheduled-events{?with_user_count}", Variables.variables(JsonObject.of("guild.id", guildId.getValue(), "with_user_count", withUserCount))))
                .flatMap(res -> res.as(GuildScheduledEvent[].class))
                .onItem().disjoint();
    }

    public Uni<GuildScheduledEvent> createGuildScheduledEvent(CreateGuildScheduledEvent createGuildScheduledEvent) {
        return requester.request(createGuildScheduledEvent.asRequest()).flatMap(res -> res.as(GuildScheduledEvent.class));
    }

    public Uni<GuildScheduledEvent> getGuildScheduledEvent(Snowflake guildId, Snowflake guildScheduledEventId, boolean withUserCount) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}{?with_user_count}", Variables.variables(JsonObject.of("guild.id", guildId.getValue(), "guild_scheduled_event.id", guildScheduledEventId.getValue(), "with_user_count", withUserCount))))
                .flatMap(res -> res.as(GuildScheduledEvent.class));
    }

    public Uni<GuildScheduledEvent> modifyGuildScheduledEvent(ModifyGuildScheduledEvent modifyGuildScheduledEvent) {
        return requester.request(modifyGuildScheduledEvent.asRequest()).flatMap(res -> res.as(GuildScheduledEvent.class));
    }

    public Uni<Void> deleteGuildScheduledEvent(Snowflake guildId, Snowflake guildScheduledEventId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/scheduled-events/{guild_scheduled_event.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("guild_scheduled_event.id", guildScheduledEventId.getValueAsString())))
                .replaceWithVoid();
    }

    public Multi<GuildScheduledEvent.UserFoo> getGuildScheduledEventUsers(GetGuildScheduledEventUsers getGuildScheduledEventUsers) {
        return requester.request(getGuildScheduledEventUsers.asRequest())
                .flatMap(res -> res.as(GuildScheduledEvent.UserFoo[].class))
                .onItem().disjoint();
    }

    public Uni<GuildTemplate> getGuildTemplate(String templateCode) {
        return requester.request(new EmptyRequest("/guilds/templates/{template.code}", Variables.variables().set("template.code", templateCode)))
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<Guild> createGuildFromGuildTemplate(CreateGuildFromGuildTemplate createGuildFromGuildTemplate) {
        return requester.request(createGuildFromGuildTemplate.asRequest()).flatMap(res -> res.as(Guild.class));
    }

    public Multi<GuildTemplate> getGuildTemplates(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/templates", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(GuildTemplate[].class))
                .onItem().disjoint();
    }

    public Uni<GuildTemplate> createGuildTemplate(CreateGuildTemplate createGuildTemplate) {
        return requester.request(createGuildTemplate.asRequest()).flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<GuildTemplate> syncGuildTemplate(Snowflake guildId, String templateCode) {
        return requester.request(new EmptyRequest(HttpMethod.PUT, "/guilds/{guild.id}/templates/{template.code}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("template.code", templateCode)))
                .flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<GuildTemplate> modifyGuildTemplate(ModifyGuildTemplate modifyGuildTemplate) {
        return requester.request(modifyGuildTemplate.asRequest()).flatMap(res -> res.as(GuildTemplate.class));
    }

    public Uni<Void> deleteGuildTemplate(Snowflake guildId, String templateCode) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/templates/{template.code}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("template.code", templateCode)))
                .replaceWithVoid();
    }

    public Uni<Invite> getInvite(GetInvite getInvite) {
        return requester.request(getInvite.asRequest()).flatMap(res -> res.as(Invite.class));
    }

    public Uni<Invite> deleteInvite(String inviteCode, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/invites/{invite.code}", Variables.variables().set("invite.code", inviteCode), auditLogReason))
                .flatMap(res -> res.as(Invite.class));
    }

    public Uni<Invite> deleteInvite(String inviteCode) {
        return deleteInvite(inviteCode, null);
    }

    public Uni<StageInstance> createStageInstance(CreateStageInstance createStageInstance) {
        return requester.request(createStageInstance.asRequest()).flatMap(res -> res.as(StageInstance.class));
    }

    public Uni<StageInstance> getStageInstance(Snowflake channelId) {
        return requester.request(new EmptyRequest("/stage-instances/{channel.id}", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .flatMap(res -> res.as(StageInstance.class));
    }

    public Uni<StageInstance> modifyStageInstance(ModifyStageInstance modifyStageInstance) {
        return requester.request(modifyStageInstance.asRequest()).flatMap(res -> res.as(StageInstance.class));
    }

    public Uni<Void> deleteStageInstance(Snowflake channelId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/stage-instances/{channel.id}", Variables.variables().set("channel.id", channelId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteStageInstance(Snowflake channelId) {
        return deleteStageInstance(channelId, null);
    }

    public Uni<Sticker> getSticker(Snowflake stickerId) {
        return requester.request(new EmptyRequest("/stickers/{sticker.id}", Variables.variables().set("sticker.id", stickerId.getValueAsString())))
                .flatMap(res -> res.as(Sticker.class));
    }

    public Multi<Sticker.Pack> listNitroStickerPacks() {
        return requester.request(new EmptyRequest("/sticker-packs")).flatMap(res -> res.as(Sticker.Pack[].class)).onItem().disjoint();
    }

    public Multi<Sticker> listGuildStickers(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/stickers", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Sticker[].class))
                .onItem().disjoint();
    }

    public Uni<Sticker> getGuildSticker(Snowflake guildId, Snowflake stickerId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/stickers/{sticker.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("sticker.id", stickerId.getValueAsString())))
                .flatMap(res -> res.as(Sticker.class));
    }

    public Uni<Sticker> createGuildSticker(CreateGuildSticker createGuildSticker) {
        return requester.request(createGuildSticker.asRequest()).flatMap(res -> res.as(Sticker.class));
    }

    public Uni<Sticker> modifyGuildSticker(ModifyGuildSticker modifyGuildSticker) {
        return requester.request(modifyGuildSticker.asRequest()).flatMap(res -> res.as(Sticker.class));
    }

    public Uni<Void> deleteGuildSticker(Snowflake guildId, Snowflake stickerId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/guilds/{guild.id}/stickers/{sticker.id}", Variables.variables().set("guild.id", guildId.getValueAsString()).set("sticker.id", stickerId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteGuildSticker(Snowflake guildId, Snowflake stickerId) {
        return deleteGuildSticker(guildId, stickerId, null);
    }

    public Uni<User> getUser(Snowflake userId) {
        return requester.request(new EmptyRequest("/users/{user.id}", Variables.variables().set("user.id", userId.getValueAsString())))
                .flatMap(res -> res.as(User.class));
    }

    public Uni<Void> leaveGuild(Snowflake guildId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/users/@me/guilds/{guild.id}", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .replaceWithVoid();
    }

    public Uni<Channel> createDm(Snowflake recipientId) {

    }

    public Multi<VoiceRegion> listVoiceRegions() {
        return requester.request(new EmptyRequest("/voice/regions")).flatMap(res -> res.as(VoiceRegion[].class)).onItem().disjoint();
    }

    public Uni<Webhook> createWebhook(CreateWebhook createWebhook) {
        return requester.request(createWebhook.asRequest()).flatMap(res -> res.as(Webhook.class));
    }

    public Multi<Webhook> getChannelWebhooks(Snowflake channelId) {
        return requester.request(new EmptyRequest("/channels/{channel.id}/webhooks", Variables.variables().set("channel.id", channelId.getValueAsString())))
                .flatMap(res -> res.as(Webhook[].class))
                .onItem().disjoint();
    }

    public Multi<Webhook> getGuildWebhooks(Snowflake guildId) {
        return requester.request(new EmptyRequest("/guilds/{guild.id}/webhooks", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Webhook[].class))
                .onItem().disjoint();
    }

    public Uni<Webhook> getWebhook(Snowflake webhookId) {
        return requester.request(new EmptyRequest("/webhooks/{webhook.id}", Variables.variables().set("webhook.id", webhookId.getValueAsString())))
                .flatMap(res -> res.as(Webhook.class));
    }

    public Uni<Webhook> modifyWebhook(ModifyWebhook modifyWebhook) {
        return requester.request(modifyWebhook.asRequest()).flatMap(res -> res.as(Webhook.class));
    }

    public Uni<Void> deleteWebhook(Snowflake webhookId, String auditLogReason) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{webhook.id}", Variables.variables().set("guild.id", webhookId.getValueAsString()), auditLogReason))
                .replaceWithVoid();
    }

    public Uni<Void> deleteWebhook(Snowflake webhookId) {
        return deleteWebhook(webhookId, null);
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordBotClient<T>> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        @SuppressWarnings("unchecked")
        public DiscordBotClient<T> build() {
            if (requesterFactory == null) {
                requesterFactory = (RequesterFactory<T>) RequesterFactory.DEFAULT_HTTP_REQUESTER;
            }
            return new DiscordBotClient<>(vertx, requesterFactory.apply(this));
        }
    }
}
