package io.disquark.rest.interactions;

import static io.disquark.rest.util.Variables.variables;
import static java.util.Objects.requireNonNull;

import java.security.Security;
import java.util.function.Supplier;

import io.disquark.rest.DiscordClient;
import io.disquark.rest.interactions.dsl.InteractionSchema;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.interaction.CreateFollowupMessageUni;
import io.disquark.rest.json.interaction.EditFollowupMessageUni;
import io.disquark.rest.json.interaction.EditOriginalInteractionResponseUni;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.request.AccessTokenSource;
import io.disquark.rest.request.EmptyRequest;
import io.disquark.rest.request.Requester;
import io.disquark.rest.request.RequesterFactory;
import io.disquark.rest.request.ratelimit.GlobalRateLimiter;
import io.disquark.rest.request.ratelimit.RateLimitStrategy;
import io.disquark.rest.response.Response;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordInteractionsClient<T extends Response> extends DiscordClient<T> implements InteractionsCapable {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordInteractionsClient.class);
    private final Router router;
    private final boolean handleCors;
    private final String interactionsUrl;
    private final boolean startHttpServer;
    private final InteractionValidator validator;
    private final Supplier<HttpServer> httpServerSupplier;

    private volatile InteractionsVerticle verticle;

    public static <T extends Response> Builder<T> builder(Vertx vertx, String verifyKey) {
        return new Builder<>(requireNonNull(vertx, "vertx"), requireNonNull(verifyKey, "verifyKey"));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordInteractionsClient<T> create(Vertx vertx, String verifyKey) {
        return (DiscordInteractionsClient<T>) builder(vertx, verifyKey).build();
    }

    private DiscordInteractionsClient(
            Vertx vertx,
            Requester<T> requester,
            Router router,
            boolean handleCors,
            String interactionsUrl,
            boolean startHttpServer,
            InteractionValidator validator,
            Supplier<HttpServer> httpServerSupplier) {
        super(vertx, requester);
        this.router = router;
        this.handleCors = handleCors;
        this.interactionsUrl = interactionsUrl;
        this.startHttpServer = startHttpServer;
        this.validator = validator;
        this.httpServerSupplier = httpServerSupplier;
    }

    private InteractionsVerticle getVerticle() {
        if (verticle == null) {
            synchronized (this) {
                if (verticle == null) {
                    verticle = new InteractionsVerticle(router, handleCors, interactionsUrl, startHttpServer,
                            httpServerSupplier, validator, this);

                    vertx.deployVerticleAndAwait(verticle);
                }
            }
        }
        return verticle;
    }

    @Override
    public <D, C extends CompletableInteraction<D>> Multi<C> on(InteractionSchema<D, C> schema) {
        return getVerticle().on(requireNonNull(schema, "schema"));
    }

    @Override
    public Uni<Message> getOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return requester.request(new EmptyRequest(
                "/webhooks/{application.id}/{interaction.token}/messages/@original", false,
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "interaction.token", requireNonNull(interactionToken, "interactionToken"))))
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public EditOriginalInteractionResponseUni editOriginalInteractionResponse(Snowflake applicationId,
            String interactionToken) {
        return (EditOriginalInteractionResponseUni) Uni.createFrom()
                .deferred(() -> new EditOriginalInteractionResponseUni(requester, applicationId, interactionToken));
    }

    @Override
    public Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/webhooks/{application.id}/{interaction.token}/messages/@original", false,
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "interaction.token", requireNonNull(interactionToken, "interactionToken"))))
                .replaceWithVoid();
    }

    @Override
    public CreateFollowupMessageUni createFollowupMessage(Snowflake applicationId, String interactionToken) {
        return (CreateFollowupMessageUni) Uni.createFrom()
                .deferred(() -> new CreateFollowupMessageUni(requester, applicationId, interactionToken));
    }

    @Override
    public Uni<Message> getFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return requester.request(new EmptyRequest(
                "/webhooks/{application.id}/{interaction.token}/messages/{message.id}", false,
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "interaction.token", requireNonNull(interactionToken, "interactionToken"), "message.id",
                        requireNonNull(messageId, "messageId").getValue())))
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public EditFollowupMessageUni editFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return (EditFollowupMessageUni) Uni.createFrom()
                .deferred(() -> new EditFollowupMessageUni(requester, applicationId, interactionToken, messageId));
    }

    @Override
    public Uni<Void> deleteFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE,
                "/webhooks/{application.id}/{interaction.token}/messages/{message.id}", false,
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                        "interaction.token", requireNonNull(interactionToken, "interactionToken"), "message.id",
                        requireNonNull(messageId, "messageId").getValue())))
                .replaceWithVoid();
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordInteractionsClient<T>> {
        protected final String verifyKey;
        protected Router router;
        protected boolean handleCors = true;
        protected String interactionsUrl;
        protected boolean startHttpServer = true;
        protected Supplier<HttpServer> httpServerSupplier;
        protected InteractionValidatorFactory validatorFactory;

        protected Builder(Vertx vertx, String verifyKey) {
            super(vertx, AccessTokenSource.DUMMY);
            this.verifyKey = verifyKey;
        }

        public Builder<T> router(Router router) {
            this.router = requireNonNull(router, "router");
            return this;
        }

        public Builder<T> handleCors(boolean handleCors) {
            this.handleCors = handleCors;
            return this;
        }

        public Builder<T> interactionsUrl(String interactionsUrl) {
            this.interactionsUrl = requireNonNull(interactionsUrl, "interactionsUrl");
            return this;
        }

        public Builder<T> startHttpServer(boolean startHttpServer) {
            this.startHttpServer = startHttpServer;
            return this;
        }

        @Override
        public Builder<T> globalRateLimiter(GlobalRateLimiter globalRateLimiter) {
            super.globalRateLimiter(globalRateLimiter);
            return this;
        }

        @Override
        public Builder<T> requesterFactory(RequesterFactory<T> requesterFactory) {
            super.requesterFactory(requesterFactory);
            return this;
        }

        @Override
        public Builder<T> rateLimitStrategy(RateLimitStrategy<T> rateLimitStrategy) {
            super.rateLimitStrategy(rateLimitStrategy);
            return this;
        }

        public Builder<T> httpServerSupplier(Supplier<HttpServer> httpServerSupplier) {
            this.httpServerSupplier = requireNonNull(httpServerSupplier, "httpServerSupplier");
            return this;
        }

        public Builder<T> validatorFactory(InteractionValidatorFactory validatorFactory) {
            this.validatorFactory = requireNonNull(validatorFactory, "validatorFactory");
            return this;
        }

        @Override
        public DiscordInteractionsClient<T> build() {
            if (validatorFactory == null) {
                validatorFactory = InteractionValidatorFactory.NO_OP;

                boolean bouncyCastle = false;
                try {
                    Class.forName("org.bouncycastle.math.ec.rfc8032.Ed25519");
                    bouncyCastle = true;
                } catch (ClassNotFoundException e) {
                    LOG.warn(
                            "org.bouncycastle dependency not installed: incoming interaction signatures will not be validated");
                }

                if (bouncyCastle && Security.getProvider("BC") == null) {
                    LOG.warn("BouncyCastle JCE provider not installed: incoming interaction signatures will not be validated");
                } else if (bouncyCastle) {
                    validatorFactory = BouncyCastleInteractionValidator::new;
                }
            }

            return new DiscordInteractionsClient<>(
                    vertx,
                    getRequesterFactory().apply(this),
                    router == null ? Router.router(vertx) : router,
                    handleCors,
                    interactionsUrl == null ? "/" : interactionsUrl,
                    startHttpServer,
                    validatorFactory.apply(verifyKey),
                    httpServerSupplier == null ? vertx::createHttpServer : httpServerSupplier);
        }
    }

    public static class Options {
        protected Router router;
        protected String verifyKey;
        protected boolean handleCors = true;
        protected String interactionsUrl;
        protected boolean startHttpServer = true;
        protected Supplier<HttpServer> httpServerSupplier;
        protected InteractionValidatorFactory validatorFactory;

        public Options setRouter(Router router) {
            this.router = requireNonNull(router, "router");
            return this;
        }

        public Router getRouter() {
            return router;
        }

        public Options setVerifyKey(String verifyKey) {
            this.verifyKey = requireNonNull(verifyKey, "verifyKey");
            return this;
        }

        public String getVerifyKey() {
            return verifyKey;
        }

        public Options setHandleCors(boolean handleCors) {
            this.handleCors = handleCors;
            return this;
        }

        public boolean handleCors() {
            return handleCors;
        }

        public Options setInteractionsUrl(String interactionsUrl) {
            this.interactionsUrl = requireNonNull(interactionsUrl, "interactionsUrl");
            return this;
        }

        public String getInteractionsUrl() {
            return interactionsUrl;
        }

        public Options setStartHttpServer(boolean startHttpServer) {
            this.startHttpServer = startHttpServer;
            return this;
        }

        public boolean startHttpServer() {
            return startHttpServer;
        }

        public Options setHttpServerSupplier(Supplier<HttpServer> httpServerSupplier) {
            this.httpServerSupplier = requireNonNull(httpServerSupplier, "httpServerSupplier");
            return this;
        }

        public Supplier<HttpServer> getHttpServerSupplier() {
            return httpServerSupplier;
        }

        public Options setValidatorFactory(InteractionValidatorFactory validatorFactory) {
            this.validatorFactory = requireNonNull(validatorFactory, "validatorFactory");
            return this;
        }

        public InteractionValidatorFactory getValidatorFactory() {
            return validatorFactory;
        }
    }
}
