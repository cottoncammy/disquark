package org.example.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.interactions.InteractionsCapable;
import org.example.rest.request.EmptyRequest;
import org.example.rest.request.Requester;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.*;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.user.GetCurrentUserGuilds;
import org.example.rest.resources.user.User;
import org.example.rest.resources.webhook.*;
import org.example.rest.response.Response;
import org.example.rest.webhook.DiscordWebhookClient;
import org.example.rest.webhook.WebhooksCapable;

import static org.example.rest.util.Variables.variables;

public abstract class AuthenticatedDiscordClient<T extends Response> extends DiscordClient<T> implements InteractionsCapable, WebhooksCapable {
    private final DiscordInteractionsClient<T> interactionsClient;
    private final DiscordWebhookClient<T> webhookClient;

    protected AuthenticatedDiscordClient(
            Vertx vertx,
            Requester<T> requester,
            DiscordInteractionsClient<T> interactionsClient,
            DiscordWebhookClient<T> webhookClient) {
        super(vertx, requester);
        this.interactionsClient = interactionsClient;
        this.webhookClient = webhookClient;
    }

    public Multi<ApplicationCommand> getGlobalApplicationCommands(Snowflake applicationId, boolean withLocalizations) {
        return requester.request(new EmptyRequest("/applications/{application.id}/commands{?with_localizations}", variables("application.id", applicationId.getValue(), "with_localizations", withLocalizations)))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Uni<ApplicationCommand> createGlobalApplicationCommand(CreateGlobalApplicationCommand createGlobalApplicationCommand) {
        return requester.request(createGlobalApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> getGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/commands/{command.id}", variables("application.id", applicationId.getValue(), "command.id", commandId.getValue())))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> editGlobalApplicationCommand(EditGlobalApplicationCommand editGlobalApplicationCommand) {
        return requester.request(editGlobalApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<Void> deleteGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/applications/{application.id}/commands/{command.id}", variables("application.id", applicationId.getValue(), "command.id", commandId.getValueAsString())))
                .replaceWithVoid();
    }

    public Multi<ApplicationCommand> bulkOverwriteGlobalApplicationCommands(BulkOverwriteGlobalApplicationCommands bulkOverwriteGlobalApplicationCommands) {
        return requester.request(bulkOverwriteGlobalApplicationCommands.asRequest())
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Multi<ApplicationCommand> getGuildApplicationCommands(Snowflake applicationId, Snowflake guildId, boolean withLocalizations) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands{?with_localizations}", variables("application.id", applicationId.getValue(), "guild.id", guildId.getValue(), "with_localizations", withLocalizations)))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Uni<ApplicationCommand> createGuildApplicationCommand(CreateGuildApplicationCommand createGuildApplicationCommand) {
        return requester.request(createGuildApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> getGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/{command.id}", variables("application.id", applicationId.getValue(), "guild.id", guildId.getValue(), "command.id", commandId.getValue())))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> editGuildApplicationCommand(EditGuildApplicationCommand editGuildApplicationCommand) {
        return requester.request(editGuildApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<Void> deleteGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}", variables("application.id", applicationId.getValue(), "guild.id", guildId.getValue(), "command.id", commandId.getValue())))
                .replaceWithVoid();
    }

    public Multi<ApplicationCommand> bulkOverwriteGuildApplicationCommands(BulkOverwriteGuildApplicationCommands bulkOverwriteGuildApplicationCommands) {
        return requester.request(bulkOverwriteGuildApplicationCommands.asRequest())
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Multi<GuildApplicationCommandPermissions> getGuildApplicationCommandPermissions(Snowflake applicationId, Snowflake guildId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/permissions", variables("application.id", applicationId.getValue(), "guild.id", guildId.getValue())))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions[].class))
                .onItem().disjoint();
    }

    public Uni<GuildApplicationCommandPermissions> getApplicationCommandPermissions(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions", variables("application.id", applicationId.getValue(), "guild.id", guildId.getValue(), "command.id", commandId.getValue())))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions.class));
    }

    public Uni<User> getCurrentUser() {
        return requester.request(new EmptyRequest("/users/@me")).flatMap(res -> res.as(User.class));
    }

    public Multi<Guild> getCurrentUserGuilds(GetCurrentUserGuilds getCurrentUserGuilds) {
        return requester.request(getCurrentUserGuilds.asRequest()).flatMap(res -> res.as(Guild[].class)).onItem().disjoint();
    }

    public Uni<Guild.Member> getCurrentUserGuildMember(Snowflake guildId) {
        return requester.request(new EmptyRequest("/users/@me/guilds/{guild.id}/member", variables("guild.id", guildId.getValue())))
                .flatMap(res -> res.as(Guild.Member.class));
    }

    @Override
    public Uni<Webhook> modifyWebhookWithToken(ModifyWebhookWithToken modifyWebhookWithToken) {
        return webhookClient.modifyWebhookWithToken(modifyWebhookWithToken);
    }

    @Override
    public Uni<Void> deleteWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return webhookClient.deleteWebhookWithToken(webhookId, webhookToken);
    }

    @Override
    public Uni<Message> executeWebhook(ExecuteWebhook executeWebhook) {
        return webhookClient.executeWebhook(executeWebhook);
    }

    @Override
    public Uni<Message> executeSlackCompatibleWebhook(ExecuteWebhookOptions options) {
        return webhookClient.executeSlackCompatibleWebhook(options);
    }

    @Override
    public Uni<Message> executeGitHubCompatibleWebhook(ExecuteWebhookOptions options) {
        return webhookClient.executeGitHubCompatibleWebhook(options);
    }

    @Override
    public Uni<Message> getWebhookMessage(WebhookMessageOptions options) {
        return webhookClient.getWebhookMessage(options);
    }

    @Override
    public Uni<Message> editWebhookMessage(EditWebhookMessage editWebhookMessage) {
        return webhookClient.editWebhookMessage(editWebhookMessage);
    }

    @Override
    public Uni<Void> deleteWebhookMessage(WebhookMessageOptions options) {
        return webhookClient.deleteWebhookMessage(options);
    }
}
