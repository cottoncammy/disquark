package org.example.rest.interactions;

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
import org.example.rest.response.Response;

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

public class DiscordInteractionsClient<T extends Response> extends DiscordClient<T> implements InteractionsCapable {

    public static <T extends Response> DiscordInteractionsClient.Builder<T> builder(Vertx vertx) {
        return new DiscordInteractionsClient.Builder<>(requireNonNull(vertx), null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordInteractionsClient<T> create(Vertx vertx) {
        return (DiscordInteractionsClient<T>) builder(vertx).build();
    }

    private DiscordInteractionsClient(Vertx vertx, Requester<T> requester) {
        super(vertx, requester);
    }

    public Uni<?> on() {

    }

    public Uni<Message> getOriginalInteractionResponse() {

    }

    public Uni<Message> editOriginalInteractionResponse() {

    }

    public Uni<Void> deleteOriginalInteractionResponse(Snowflake applicationId, String interactionToken) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/webhooks/{application.id}/{interaction.token}/messages/@original", variables("application.id", applicationId.getValue(), "interaction.token", interactionToken)))
                .replaceWithVoid();
    }

    public Uni<Message> createFollowupMessage(CreateFollowupMessage createFollowupMessage) {
        return requester.request(createFollowupMessage.asRequest()).flatMap(res -> res.as(Message.class));
    }

    public Uni<Message> getFollowupMessage() {

    }

    public Uni<Message> editFollowupMessage() {

    }

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
