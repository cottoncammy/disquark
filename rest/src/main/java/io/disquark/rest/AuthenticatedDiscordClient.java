package io.disquark.rest;

import static io.disquark.rest.util.Variables.variables;
import static java.util.Objects.requireNonNull;

import io.disquark.rest.interactions.CompletableInteraction;
import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.interactions.InteractionsCapable;
import io.disquark.rest.interactions.dsl.InteractionSchema;
import io.disquark.rest.request.AccessTokenSource;
import io.disquark.rest.request.EmptyRequest;
import io.disquark.rest.request.Requester;
import io.disquark.rest.request.RequesterFactory;
import io.disquark.rest.request.ratelimit.BucketRateLimitingRequester;
import io.disquark.rest.request.ratelimit.GlobalRateLimiter;
import io.disquark.rest.request.ratelimit.RateLimitStrategy;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.application.command.ApplicationCommand;
import io.disquark.rest.resources.application.command.BulkOverwriteGlobalApplicationCommands;
import io.disquark.rest.resources.application.command.BulkOverwriteGuildApplicationCommands;
import io.disquark.rest.resources.application.command.CreateGlobalApplicationCommand;
import io.disquark.rest.resources.application.command.CreateGuildApplicationCommand;
import io.disquark.rest.resources.application.command.EditGlobalApplicationCommand;
import io.disquark.rest.resources.application.command.EditGuildApplicationCommand;
import io.disquark.rest.resources.application.command.GuildApplicationCommandPermissions;
import io.disquark.rest.resources.channel.message.Message;
import io.disquark.rest.resources.interactions.CreateFollowupMessage;
import io.disquark.rest.resources.interactions.EditFollowupMessage;
import io.disquark.rest.resources.interactions.EditOriginalInteractionResponse;
import io.disquark.rest.resources.partial.PartialGuild;
import io.disquark.rest.resources.user.GetCurrentUserGuilds;
import io.disquark.rest.resources.user.User;
import io.disquark.rest.resources.webhook.EditWebhookMessage;
import io.disquark.rest.resources.webhook.ExecuteWebhook;
import io.disquark.rest.resources.webhook.ExecuteWebhookOptions;
import io.disquark.rest.resources.webhook.ModifyWebhookWithToken;
import io.disquark.rest.resources.webhook.Webhook;
import io.disquark.rest.resources.webhook.WebhookMessageOptions;
import io.disquark.rest.response.Response;
import io.disquark.rest.webhook.DiscordWebhookClient;
import io.disquark.rest.webhook.WebhooksCapable;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;

public abstract class AuthenticatedDiscordClient<T extends Response> extends DiscordClient<T>
        implements InteractionsCapable, WebhooksCapable {
    private final DiscordWebhookClient<T> webhookClient;

    protected final DiscordInteractionsClient.Options interactionsClientOptions;

    protected volatile DiscordInteractionsClient<T> interactionsClient;

    @SuppressWarnings("unchecked")
    protected AuthenticatedDiscordClient(
            Vertx vertx,
            Requester<T> requester,
            DiscordInteractionsClient.Options interactionsClientOptions) {
        super(vertx, requester);
        this.webhookClient = ((DiscordWebhookClient.Builder<T>) DiscordWebhookClient.builder(vertx))
                .requesterFactory(x -> wrapRequester())
                .build();
        this.interactionsClientOptions = interactionsClientOptions;
    }

    @SuppressWarnings("unchecked")
    private Requester<T> wrapRequester() {
        if (requester instanceof BucketRateLimitingRequester) {
            return (Requester<T>) new BucketRateLimitingRequester(((BucketRateLimitingRequester) requester).getRequester());
        }
        return requester;
    }

    public Multi<ApplicationCommand> getGlobalApplicationCommands(Snowflake applicationId, boolean withLocalizations) {
        return requester.request(new EmptyRequest("/applications/{application.id}/commands{?with_localizations}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "with_localizations", withLocalizations)))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Uni<ApplicationCommand> createGlobalApplicationCommand(
            CreateGlobalApplicationCommand createGlobalApplicationCommand) {
        return requester.request(requireNonNull(createGlobalApplicationCommand, "createGlobalApplicationCommand").asRequest())
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> getGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/commands/{command.id}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(), "command.id",
                        requireNonNull(commandId, "commandId").getValue())))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> editGlobalApplicationCommand(EditGlobalApplicationCommand editGlobalApplicationCommand) {
        return requester.request(requireNonNull(editGlobalApplicationCommand, "editGlobalApplicationCommand").asRequest())
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<Void> deleteGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/applications/{application.id}/commands/{command.id}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(), "command.id",
                        requireNonNull(commandId, "commandId").getValue())))
                .replaceWithVoid();
    }

    public Multi<ApplicationCommand> bulkOverwriteGlobalApplicationCommands(
            BulkOverwriteGlobalApplicationCommands bulkOverwriteGlobalApplicationCommands) {
        return requester
                .request(requireNonNull(bulkOverwriteGlobalApplicationCommands, "bulkOverwriteGlobalApplicationCommands")
                        .asRequest())
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Multi<ApplicationCommand> getGuildApplicationCommands(Snowflake applicationId, Snowflake guildId,
            boolean withLocalizations) {
        return requester
                .request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands{?with_localizations}",
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(), "guild.id",
                                requireNonNull(guildId, "guildId").getValue(), "with_localizations", withLocalizations)))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Uni<ApplicationCommand> createGuildApplicationCommand(CreateGuildApplicationCommand createGuildApplicationCommand) {
        return requester.request(requireNonNull(createGuildApplicationCommand, "createGuildApplicationCommand").asRequest())
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> getGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/{command.id}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(), "guild.id",
                        requireNonNull(guildId, "guildId").getValue(), "command.id",
                        requireNonNull(commandId, "commandId").getValue())))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> editGuildApplicationCommand(EditGuildApplicationCommand editGuildApplicationCommand) {
        return requester.request(requireNonNull(editGuildApplicationCommand, "editGuildApplicationCommand").asRequest())
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<Void> deleteGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(), "guild.id",
                        requireNonNull(guildId, "guildId").getValue(), "command.id",
                        requireNonNull(commandId, "commandId").getValue())))
                .replaceWithVoid();
    }

    public Multi<ApplicationCommand> bulkOverwriteGuildApplicationCommands(
            BulkOverwriteGuildApplicationCommands bulkOverwriteGuildApplicationCommands) {
        return requester.request(requireNonNull(bulkOverwriteGuildApplicationCommands, "bulkOverwriteGuildApplicationCommands")
                .asRequest())
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Multi<GuildApplicationCommandPermissions> getGuildApplicationCommandPermissions(Snowflake applicationId,
            Snowflake guildId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/permissions",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(), "guild.id",
                        requireNonNull(guildId, "guildId").getValue())))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions[].class))
                .onItem().disjoint();
    }

    public Uni<GuildApplicationCommandPermissions> getApplicationCommandPermissions(Snowflake applicationId, Snowflake guildId,
            Snowflake commandId) {
        return requester
                .request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions",
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(), "guild.id",
                                requireNonNull(guildId, "guildId").getValue(), "command.id",
                                requireNonNull(commandId, "commandId").getValue())))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions.class));
    }

    protected DiscordInteractionsClient<T> buildInteractionsClient(DiscordInteractionsClient.Builder<T> builder) {
        if (interactionsClientOptions.getRouter() != null) {
            builder.router(interactionsClientOptions.getRouter());
        }

        if (interactionsClientOptions.getHttpServer() != null) {
            builder.httpServer(interactionsClientOptions.getHttpServer());
        }

        if (interactionsClientOptions.getInteractionsUrl() != null) {
            builder.interactionsUrl(interactionsClientOptions.getInteractionsUrl());
        }

        if (interactionsClientOptions.getValidatorFactory() != null) {
            builder.validatorFactory(interactionsClientOptions.getValidatorFactory());
        }

        return builder.requesterFactory(x -> webhookClient.requester).build();
    }

    protected abstract DiscordInteractionsClient<T> buildInteractionsClient();

    private DiscordInteractionsClient<T> getInteractionsClient() {
        if (interactionsClient == null) {
            synchronized (this) {
                if (interactionsClient == null) {
                    interactionsClient = buildInteractionsClient();
                }
            }
        }
        return interactionsClient;
    }

    @Override
    public <D, C extends CompletableInteraction<D>> Multi<C> on(InteractionSchema<D, C> schema) {
        return getInteractionsClient().on(schema);
    }

    @Override
    public Uni<Message> getOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return getInteractionsClient().getOriginalInteractionResponse(applicationId, interactionToken);
    }

    @Override
    public Uni<Message> editOriginalInteractionResponse(EditOriginalInteractionResponse editOriginalInteractionResponse) {
        return getInteractionsClient().editOriginalInteractionResponse(editOriginalInteractionResponse);
    }

    @Override
    public Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return getInteractionsClient().deleteOriginalInteractionResponse(applicationId, interactionToken);
    }

    @Override
    public Uni<Message> createFollowupMessage(CreateFollowupMessage createFollowupMessage) {
        return getInteractionsClient().createFollowupMessage(createFollowupMessage);
    }

    @Override
    public Uni<Message> getFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return getInteractionsClient().getFollowupMessage(applicationId, interactionToken, messageId);
    }

    @Override
    public Uni<Message> editFollowupMessage(EditFollowupMessage editFollowupMessage) {
        return getInteractionsClient().editFollowupMessage(editFollowupMessage);
    }

    @Override
    public Uni<Void> deleteFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return getInteractionsClient().deleteFollowupMessage(applicationId, interactionToken, messageId);
    }

    public Uni<User> getCurrentUser() {
        return requester.request(new EmptyRequest("/users/@me")).flatMap(res -> res.as(User.class));
    }

    public Multi<PartialGuild> getCurrentUserGuilds(GetCurrentUserGuilds getCurrentUserGuilds) {
        return requester.request(requireNonNull(getCurrentUserGuilds, "getCurrentUserGuilds").asRequest())
                .flatMap(res -> res.as(PartialGuild[].class)).onItem().disjoint();
    }

    @Override
    public Uni<Webhook> getWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return webhookClient.getWebhookWithToken(webhookId, webhookToken);
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

    public static abstract class Builder<R extends Response, T extends AuthenticatedDiscordClient<R>>
            extends DiscordClient.Builder<R, T> {
        private DiscordInteractionsClient.Options interactionsClientOptions;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        public Builder<R, T> globalRateLimiter(GlobalRateLimiter globalRateLimiter) {
            super.globalRateLimiter(globalRateLimiter);
            return this;
        }

        @Override
        public Builder<R, T> requesterFactory(RequesterFactory<R> requesterFactory) {
            super.requesterFactory(requesterFactory);
            return this;
        }

        @Override
        public Builder<R, T> rateLimitStrategy(RateLimitStrategy<R> rateLimitStrategy) {
            super.rateLimitStrategy(rateLimitStrategy);
            return this;
        }

        public Builder<R, T> interactionsClientOptions(DiscordInteractionsClient.Options interactionsClientOptions) {
            this.interactionsClientOptions = requireNonNull(interactionsClientOptions, "interactionsClientOptions");
            return this;
        }

        protected DiscordInteractionsClient.Options getInteractionsClientOptions() {
            return (interactionsClientOptions == null) ? new DiscordInteractionsClient.Options() : interactionsClientOptions;
        }
    }
}
