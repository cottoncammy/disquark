package org.example.rest.interactions;

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

import java.security.Security;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;

import org.example.rest.DiscordClient;
import org.example.rest.interactions.dsl.InteractionSchema;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.EmptyRequest;
import org.example.rest.request.Requester;
import org.example.rest.request.RequesterFactory;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.CreateFollowupMessage;
import org.example.rest.resources.interactions.EditFollowupMessage;
import org.example.rest.resources.interactions.EditOriginalInteractionResponse;
import org.example.rest.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordInteractionsClient<T extends Response> extends DiscordClient<T> implements InteractionsCapable {
    private static final Logger LOG = LoggerFactory.getLogger(DiscordInteractionsClient.class);
    private final Router router;
    private final HttpServer httpServer;
    private final String interactionsUrl;
    private final InteractionValidator validator;

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
            HttpServer httpServer,
            String interactionsUrl,
            InteractionValidator validator) {
        super(vertx, requester);
        this.router = router;
        this.httpServer = httpServer;
        this.interactionsUrl = interactionsUrl;
        this.validator = validator;
    }

    private InteractionsVerticle getVerticle() {
        if (verticle == null) {
            synchronized (this) {
                if (verticle == null) {
                    verticle = new InteractionsVerticle(router, httpServer, interactionsUrl, validator, this);
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
        return requester
                .request(new EmptyRequest("/webhooks/{application.id}/{interaction.token}/messages/@original", false,
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                                "interaction.token", requireNonNull(interactionToken, "interactionToken"))))
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> editOriginalInteractionResponse(EditOriginalInteractionResponse editOriginalInteractionResponse) {
        return requester.request(requireNonNull(editOriginalInteractionResponse, "editOriginalInteractionResponse").asRequest())
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return requester
                .request(new EmptyRequest(HttpMethod.DELETE,
                        "/webhooks/{application.id}/{interaction.token}/messages/@original", false,
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                                "interaction.token", requireNonNull(interactionToken, "interactionToken"))))
                .replaceWithVoid();
    }

    @Override
    public Uni<Message> createFollowupMessage(CreateFollowupMessage createFollowupMessage) {
        return requester.request(requireNonNull(createFollowupMessage, "createFollowupMessage").asRequest())
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> getFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return requester
                .request(new EmptyRequest("/webhooks/{application.id}/{interaction.token}/messages/{message.id}", false,
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                                "interaction.token", requireNonNull(interactionToken, "interactionToken"), "message.id",
                                requireNonNull(messageId, "messageId").getValue())))
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> editFollowupMessage(EditFollowupMessage editFollowupMessage) {
        return requester.request(requireNonNull(editFollowupMessage, "editFollowupMessage").asRequest())
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Void> deleteFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return requester
                .request(new EmptyRequest(HttpMethod.DELETE,
                        "/webhooks/{application.id}/{interaction.token}/messages/{message.id}", false,
                        variables("application.id", requireNonNull(applicationId, "applicationId").getValue(),
                                "interaction.token", requireNonNull(interactionToken, "interactionToken"), "message.id",
                                requireNonNull(messageId, "messageId").getValue())))
                .replaceWithVoid();
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordInteractionsClient<T>> {
        protected final String verifyKey;
        protected Router router;
        protected HttpServer httpServer;
        protected String interactionsUrl;
        protected InteractionValidatorFactory validatorFactory;

        protected Builder(Vertx vertx, String verifyKey) {
            super(vertx, AccessTokenSource.DUMMY);
            this.verifyKey = verifyKey;
        }

        public Builder<T> router(Router router) {
            this.router = requireNonNull(router, "router");
            return this;
        }

        public Builder<T> httpServer(HttpServer httpServer) {
            this.httpServer = requireNonNull(httpServer, "httpServer");
            return this;
        }

        public Builder<T> interactionsUrl(String interactionsUrl) {
            this.interactionsUrl = requireNonNull(interactionsUrl, "interactionsUrl");
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

        public Builder<T> validatorFactory(InteractionValidatorFactory validatorFactory) {
            this.validatorFactory = requireNonNull(validatorFactory, "validatorFactory");
            return this;
        }

        @Override
        public DiscordInteractionsClient<T> build() {
            if (validatorFactory == null) {
                validatorFactory = InteractionValidatorFactory.NO_OP;

                boolean noBouncyCastle = false;
                try {
                    Class.forName("org.bouncycastle.math.ec.rfc8032.Ed25519");
                } catch (ClassNotFoundException e) {
                    LOG.warn(
                            "org.bouncycastle dependency not installed: incoming interaction signatures will not be validated");
                    noBouncyCastle = true;
                }

                if (!noBouncyCastle && Security.getProvider("BC") == null) {
                    LOG.warn("BouncyCastle JCE provider not installed: incoming interaction signatures will not be validated");
                } else {
                    validatorFactory = BouncyCastleInteractionValidator::new;
                }
            }

            return new DiscordInteractionsClient<>(
                    vertx,
                    getRequesterFactory().apply(this),
                    router == null ? Router.router(vertx) : router,
                    httpServer == null ? vertx.createHttpServer() : httpServer,
                    interactionsUrl == null ? "/" : interactionsUrl,
                    validatorFactory.apply(verifyKey));
        }
    }

    public static class Options {
        protected Router router;
        protected String verifyKey;
        protected HttpServer httpServer;
        protected String interactionsUrl;
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

        public Options setHttpServer(HttpServer httpServer) {
            this.httpServer = requireNonNull(httpServer, "httpServer");
            return this;
        }

        public HttpServer getHttpServer() {
            return httpServer;
        }

        public Options setInteractionsUrl(String interactionsUrl) {
            this.interactionsUrl = requireNonNull(interactionsUrl, "interactionsUrl");
            return this;
        }

        public String getInteractionsUrl() {
            return interactionsUrl;
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
