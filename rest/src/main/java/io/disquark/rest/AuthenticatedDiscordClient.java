package io.disquark.rest;

import static io.disquark.rest.util.Variables.variables;
import static java.util.Objects.requireNonNull;

import io.disquark.rest.interactions.CompletableInteraction;
import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.interactions.InteractionsCapable;
import io.disquark.rest.interactions.dsl.InteractionSchema;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.command.ApplicationCommand;
import io.disquark.rest.json.command.BulkOverwriteGlobalApplicationCommandsMulti;
import io.disquark.rest.json.command.BulkOverwriteGuildApplicationCommandsMulti;
import io.disquark.rest.json.command.CreateGlobalApplicationCommandUni;
import io.disquark.rest.json.command.CreateGuildApplicationCommandUni;
import io.disquark.rest.json.command.EditGlobalApplicationCommandUni;
import io.disquark.rest.json.command.EditGuildApplicationCommandUni;
import io.disquark.rest.json.command.GuildApplicationCommandPermissions;
import io.disquark.rest.json.guild.GetCurrentUserGuildsMulti;
import io.disquark.rest.json.interaction.CreateFollowupMessageUni;
import io.disquark.rest.json.interaction.EditFollowupMessageUni;
import io.disquark.rest.json.interaction.EditOriginalInteractionResponseUni;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.user.User;
import io.disquark.rest.json.webhook.DeleteWebhookMessageUni;
import io.disquark.rest.json.webhook.EditWebhookMessageUni;
import io.disquark.rest.json.webhook.ExecuteWebhookUni;
import io.disquark.rest.json.webhook.GetWebhookMessageUni;
import io.disquark.rest.json.webhook.ModifyWebhookWithTokenUni;
import io.disquark.rest.json.webhook.Webhook;
import io.disquark.rest.request.AccessTokenSource;
import io.disquark.rest.request.EmptyRequest;
import io.disquark.rest.request.Requester;
import io.disquark.rest.request.RequesterFactory;
import io.disquark.rest.request.ratelimit.BucketRateLimitingRequester;
import io.disquark.rest.request.ratelimit.GlobalRateLimiter;
import io.disquark.rest.request.ratelimit.RateLimitStrategy;
import io.disquark.rest.response.Response;
import io.disquark.rest.webhook.DiscordWebhookClient;
import io.disquark.rest.webhook.WebhooksCapable;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;

public abstract class AuthenticatedDiscordClient<T extends Response> extends DiscordClient<T>
        implements InteractionsCapable, WebhooksCapable {
    protected final DiscordInteractionsClient.Options interactionsClientOptions;

    private volatile DiscordWebhookClient<T> webhookClient;
    private volatile DiscordInteractionsClient<T> interactionsClient;

    protected AuthenticatedDiscordClient(
            Vertx vertx,
            Requester<T> requester,
            DiscordInteractionsClient.Options interactionsClientOptions) {
        super(vertx, requester);
        this.interactionsClientOptions = interactionsClientOptions;
    }

    public Multi<ApplicationCommand> getGlobalApplicationCommands(Snowflake applicationId, boolean withLocalizations) {
        return deferredUni(
                () -> requester.request(new EmptyRequest("/applications/{application.id}/commands{?with_localizations}",
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                                "with_localizations", withLocalizations))))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public CreateGlobalApplicationCommandUni createGlobalApplicationCommand(Snowflake applicationId, String name) {
        return (CreateGlobalApplicationCommandUni) deferredUni(
                () -> new CreateGlobalApplicationCommandUni(requester, applicationId, name));
    }

    public Uni<ApplicationCommand> getGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/applications/{application.id}/commands/{command.id}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "command.id", requireNonNull(commandId, "commandId").getValue()))))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public EditGlobalApplicationCommandUni editGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return (EditGlobalApplicationCommandUni) deferredUni(
                () -> new EditGlobalApplicationCommandUni(requester, applicationId, commandId));
    }

    public Uni<Void> deleteGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return deferredUni(() -> requester
                .request(new EmptyRequest(HttpMethod.DELETE, "/applications/{application.id}/commands/{command.id}",
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                                "command.id", requireNonNull(commandId, "commandId").getValue()))))
                .replaceWithVoid();
    }

    public BulkOverwriteGlobalApplicationCommandsMulti bulkOverwriteGlobalApplicationCommands(Snowflake applicationId) {
        return (BulkOverwriteGlobalApplicationCommandsMulti) deferredMulti(
                () -> new BulkOverwriteGlobalApplicationCommandsMulti(requester, applicationId));
    }

    public Multi<ApplicationCommand> getGuildApplicationCommands(Snowflake applicationId, Snowflake guildId,
            boolean withLocalizations) {
        return deferredUni(() -> requester.request(new EmptyRequest(
                "/applications/{application.id}/guilds/{guild.id}/commands{?with_localizations}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "with_localizations", withLocalizations))))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public CreateGuildApplicationCommandUni createGuildApplicationCommand(Snowflake applicationId, Snowflake guildId,
            String name) {
        return (CreateGuildApplicationCommandUni) deferredUni(
                () -> new CreateGuildApplicationCommandUni(requester, applicationId, guildId, name));
    }

    public Uni<ApplicationCommand> getGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return deferredUni(() -> requester.request(new EmptyRequest(
                "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "command.id", requireNonNull(commandId, "commandId").getValue()))))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public EditGuildApplicationCommandUni editGuildApplicationCommand(Snowflake applicationId, Snowflake guildId,
            Snowflake commandId) {
        return (EditGuildApplicationCommandUni) deferredUni(
                () -> new EditGuildApplicationCommandUni(requester, applicationId, guildId, commandId));
    }

    public Uni<Void> deleteGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return deferredUni(() -> requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "command.id", requireNonNull(commandId, "commandId").getValue()))))
                .replaceWithVoid();
    }

    public BulkOverwriteGuildApplicationCommandsMulti bulkOverwriteGuildApplicationCommands(Snowflake applicationId,
            Snowflake guildId) {
        return (BulkOverwriteGuildApplicationCommandsMulti) deferredMulti(
                () -> new BulkOverwriteGuildApplicationCommandsMulti(requester, applicationId, guildId));
    }

    public Multi<GuildApplicationCommandPermissions> getGuildApplicationCommandPermissions(Snowflake applicationId,
            Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest(
                "/applications/{application.id}/guilds/{guild.id}/commands/permissions",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions[].class))
                .onItem().disjoint();
    }

    public Uni<GuildApplicationCommandPermissions> getApplicationCommandPermissions(Snowflake applicationId, Snowflake guildId,
            Snowflake commandId) {
        return deferredUni(() -> requester.request(new EmptyRequest(
                "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "guild.id", requireNonNull(guildId, "guildId").getValue(),
                        "command.id", requireNonNull(commandId, "commandId").getValue()))))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions.class));
    }

    protected DiscordInteractionsClient<T> buildInteractionsClient(DiscordInteractionsClient.Builder<T> builder) {
        if (interactionsClientOptions.getRouter() != null) {
            builder.router(interactionsClientOptions.getRouter());
        }

        if (interactionsClientOptions.getInteractionsUrl() != null) {
            builder.interactionsUrl(interactionsClientOptions.getInteractionsUrl());
        }

        if (interactionsClientOptions.getHttpServerSupplier() != null) {
            builder.httpServerSupplier(interactionsClientOptions.getHttpServerSupplier());
        }

        if (interactionsClientOptions.getValidatorFactory() != null) {
            builder.validatorFactory(interactionsClientOptions.getValidatorFactory());
        }

        return builder.handleCors(interactionsClientOptions.handleCors())
                .startHttpServer(interactionsClientOptions.startHttpServer())
                .requesterFactory(x -> getWebhookClient().requester)
                .build();
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
    public EditOriginalInteractionResponseUni editOriginalInteractionResponse(Snowflake applicationId,
            String interactionToken) {
        return getInteractionsClient().editOriginalInteractionResponse(applicationId, interactionToken);
    }

    @Override
    public Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return getInteractionsClient().deleteOriginalInteractionResponse(applicationId, interactionToken);
    }

    @Override
    public CreateFollowupMessageUni createFollowupMessage(Snowflake applicationId, String interactionToken) {
        return getInteractionsClient().createFollowupMessage(applicationId, interactionToken);
    }

    @Override
    public Uni<Message> getFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return getInteractionsClient().getFollowupMessage(applicationId, interactionToken, messageId);
    }

    @Override
    public EditFollowupMessageUni editFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return getInteractionsClient().editFollowupMessage(applicationId, interactionToken, messageId);
    }

    @Override
    public Uni<Void> deleteFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return getInteractionsClient().deleteFollowupMessage(applicationId, interactionToken, messageId);
    }

    public Uni<User> getCurrentUser() {
        return requester.request(new EmptyRequest("/users/@me")).flatMap(res -> res.as(User.class));
    }

    public GetCurrentUserGuildsMulti getCurrentUserGuilds() {
        return new GetCurrentUserGuildsMulti(requester);
    }

    @SuppressWarnings("unchecked")
    private Requester<T> wrapRequester() {
        if (requester instanceof BucketRateLimitingRequester) {
            return (Requester<T>) new BucketRateLimitingRequester(((BucketRateLimitingRequester) requester).getRequester());
        }
        return requester;
    }

    private DiscordWebhookClient<T> getWebhookClient() {
        if (webhookClient == null) {
            synchronized (this) {
                if (webhookClient == null) {
                    webhookClient = DiscordWebhookClient.<T> builder(vertx)
                            .requesterFactory(x -> wrapRequester())
                            .build();
                }
            }
        }
        return webhookClient;
    }

    @Override
    public Uni<Webhook> getWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return getWebhookClient().getWebhookWithToken(webhookId, webhookToken);
    }

    @Override
    public ModifyWebhookWithTokenUni modifyWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return getWebhookClient().modifyWebhookWithToken(webhookId, webhookToken);
    }

    @Override
    public Uni<Void> deleteWebhookWithToken(Snowflake webhookId, String webhookToken) {
        return getWebhookClient().deleteWebhookWithToken(webhookId, webhookToken);
    }

    @Override
    public ExecuteWebhookUni executeWebhook(Snowflake webhookId, String webhookToken) {
        return getWebhookClient().executeWebhook(webhookId, webhookToken);
    }

    @Override
    public GetWebhookMessageUni getWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId) {
        return getWebhookClient().getWebhookMessage(webhookId, webhookToken, messageId);
    }

    @Override
    public EditWebhookMessageUni editWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId) {
        return getWebhookClient().editWebhookMessage(webhookId, webhookToken, messageId);
    }

    @Override
    public DeleteWebhookMessageUni deleteWebhookMessage(Snowflake webhookId, String webhookToken, Snowflake messageId) {
        return getWebhookClient().deleteWebhookMessage(webhookId, webhookToken, messageId);
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
