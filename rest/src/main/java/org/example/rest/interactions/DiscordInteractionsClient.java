package org.example.rest.interactions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.DiscordClient;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.EmptyRequest;
import org.example.rest.request.Requester;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.interactions.CreateFollowupMessage;
import org.example.rest.resources.interactions.EditFollowupMessage;
import org.example.rest.resources.interactions.EditOriginalInteractionResponse;
import org.example.rest.response.Response;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

public class DiscordInteractionsClient<T extends Response> extends DiscordClient<T> implements InteractionsCapable {
    private final Options options;
    private final InteractionsVerticle verticle;

    protected void create(AuthenticatedDiscordClient<?> discordClient) {

    }

    public static <T extends Response> Builder<T> builder(Vertx vertx, String verifyKey) {
        return new Builder<>(requireNonNull(vertx), null, requireNonNull(verifyKey));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordInteractionsClient<T> create(Vertx vertx, String verifyKey) {
        return (DiscordInteractionsClient<T>) builder(vertx, verifyKey).build();
    }

    private DiscordInteractionsClient(Vertx vertx, Requester<T> requester, Options options) {
        super(vertx, requester);
        this.options = options;
        this.verticle = new InteractionsVerticle();

        vertx.deployVerticleAndAwait(verticle);
    }

    Options getOptions() {
        return options;
    }

    public <T> Multi<CompletableInteraction<T>> on() {
        return verticle.on();
    }

    @Override
    public Uni<Message> getOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return requester.request(new EmptyRequest("/webhooks/{application.id}/{interaction.token}/messages/@original", variables("application.id", applicationId.getValue(), "interaction.token", interactionToken)))
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> editOriginalInteractionResponse(EditOriginalInteractionResponse editOriginalInteractionResponse) {
        return requester.request(editOriginalInteractionResponse.asRequest()).flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{application.id}/{interaction.token}/messages/@original", variables("application.id", applicationId.getValue(), "interaction.token", interactionToken)))
                .replaceWithVoid();
    }

    @Override
    public Uni<Message> createFollowupMessage(CreateFollowupMessage createFollowupMessage) {
        return requester.request(createFollowupMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> getFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return requester.request(new EmptyRequest("/webhooks/{application.id}/{interaction.token}/messages/{message.id}", variables("application.id", applicationId.getValue(), "interaction.token", interactionToken, "message.id", messageId)))
                .flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Message> editFollowupMessage(EditFollowupMessage editFollowupMessage) {
        return requester.request(editFollowupMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    @Override
    public Uni<Void> deleteFollowupMessage(Snowflake applicationId, String interactionToken, Snowflake messageId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{application.id}/{interaction.token}/messages/{message.id}", variables("application.id", applicationId.getValue(), "interaction.token", interactionToken, "message.id", messageId)))
                .replaceWithVoid();
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordInteractionsClient<T>> {
        protected final String verifyKey;
        protected Router router;
        protected ServerCodec jsonCodec;
        protected HttpServer httpServer;
        protected String interactionsUrl;
        protected Function<Uni<String>, InteractionValidator> validatorFactory;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource, String verifyKey) {
            super(vertx, tokenSource);
            this.verifyKey = verifyKey;
        }

        public Builder<T> router(Router router) {
            this.router = requireNonNull(router);
            return this;
        }

        public Builder<T> jsonCodec(ServerCodec jsonCodec) {
            this.jsonCodec = requireNonNull(jsonCodec);
            return this;
        }

        public Builder<T> httpServer(HttpServer httpServer) {
            this.httpServer = requireNonNull(httpServer);
            return this;
        }

        public Builder<T> interactionsUrl(String interactionsUrl) {
            this.interactionsUrl = requireNonNull(interactionsUrl);
            return this;
        }

        public Builder<T> validatorFactory(Function<Uni<String>, InteractionValidator> validatorFactory) {
            this.validatorFactory = requireNonNull(validatorFactory);
            return this;
        }

        @Override
        public DiscordInteractionsClient<T> build() {
            // TODO
            return new DiscordInteractionsClient<>(vertx, getRequesterFactory().apply(this), null);
        }
    }

    public static class Options {
        protected Router router;
        protected ServerCodec jsonCodec;
        protected HttpServer httpServer;
        protected String interactionsUrl;
        protected Function<Uni<String>, InteractionValidator> validatorFactory;

        public Options router(Router router) {
            this.router = requireNonNull(router);
            return this;
        }

        Router getRouter() {
            return router;
        }

        public Options jsonCodec(ServerCodec jsonCodec) {
            this.jsonCodec = requireNonNull(jsonCodec);
            return this;
        }

        ServerCodec getJsonCodec() {
            return jsonCodec;
        }

        public Options httpServer(HttpServer httpServer) {
            this.httpServer = requireNonNull(httpServer);
            return this;
        }

        HttpServer getHttpServer() {
            return httpServer;
        }

        public Options interactionsUrl(String interactionsUrl) {
            this.interactionsUrl = requireNonNull(interactionsUrl);
            return this;
        }

        String getInteractionsUrl() {
            return interactionsUrl;
        }

        public Options validatorFactory(Function<Uni<String>, InteractionValidator> validatorFactory) {
            this.validatorFactory = requireNonNull(validatorFactory);
            return this;
        }

        Function<Uni<String>, InteractionValidator> getValidatorFactory() {
            return validatorFactory;
        }
    }
}
