package org.example.rest.interactions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
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

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

public class DiscordInteractionsClient<T extends Response> extends DiscordClient<T> implements InteractionsCapable {
    private final InteractionsVerticle verticle;

    public static <T extends Response> DiscordInteractionsClient.Builder<T> builder(Vertx vertx) {
        return new DiscordInteractionsClient.Builder<>(requireNonNull(vertx), null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordInteractionsClient<T> create(Vertx vertx) {
        return (DiscordInteractionsClient<T>) builder(vertx).build();
    }

    private DiscordInteractionsClient(Vertx vertx, Requester<T> requester) {
        super(vertx, requester);
        // TODO deploy it
        this.verticle = new InteractionsVerticle();
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

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        public DiscordInteractionsClient<T> build() {
            return null;
        }
    }
}
