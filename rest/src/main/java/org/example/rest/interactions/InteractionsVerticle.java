package org.example.rest.interactions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.core.http.HttpServerRequest;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.example.rest.request.codec.Codec;
import org.example.rest.resources.interactions.Interaction;

import java.util.function.Consumer;

class InteractionsVerticle extends AbstractVerticle {
    private final Router router;
    private final Codec jsonCodec;
    private final HttpServer httpServer;
    private final String interactionsUrl;
    private final InteractionValidator interactionValidator;
    private final DiscordInteractionsClient<?> interactionsClient;
    private final BroadcastProcessor<RoutingContext> processor = BroadcastProcessor.create();

    public InteractionsVerticle(
            Router router,
            Codec jsonCodec,
            String interactionsUrl,
            HttpServer httpServer,
            InteractionValidator interactionValidator,
            DiscordInteractionsClient<?> interactionsClient) {
        this.router = router;
        this.jsonCodec = jsonCodec;
        this.interactionsUrl = interactionsUrl;
        this.httpServer = httpServer;
        this.interactionValidator = interactionValidator;
        this.interactionsClient = interactionsClient;
    }

    private Consumer<RoutingContext> requestHandler() {
        return new Consumer<RoutingContext>() {
            @Override
            @SuppressWarnings("unchecked")
            public void accept(RoutingContext context) {
                HttpServerRequest request = context.request();
                String signature = request.getHeader("X-Signature-Ed25519");
                String timestamp = request.getHeader("X-Signature-Timestamp");

                request.body()
                        .call(body -> {
                            if (!interactionValidator.validate(timestamp, body.toString(), signature)) {
                                return Uni.createFrom().failure(UnauthorizedException::new);
                            }
                            return Uni.createFrom().voidItem();
                        })
                        .onFailure(UnauthorizedException.class).invoke(() -> context.fail(401))
                        .map(body -> jsonCodec.deserialize(body, Interaction.class))
                        .onFailure(IllegalArgumentException.class).invoke(() -> context.fail(400))
                        .call(interaction -> {
                            if (interaction.type() == Interaction.Type.PING) {
                                return new CompletablePingInteraction(interaction, context.response(), interactionsClient).pong();
                            }
                            return Uni.createFrom().voidItem();
                        })
                        .invoke(interaction -> {
                            context.put("interaction", interaction);
                            processor.onNext(context);
                        })
                        .onFailure().recoverWithNull()
                        .subscribe()
                        .with(x -> {});
            }
        };
    }

    @Override
    public Uni<Void> asyncStart() {
        return Uni.createFrom().item(router.route(HttpMethod.POST, interactionsUrl).handler(requestHandler()))
                .replaceWith(httpServer.requestHandler(router))
                .call(() -> httpServer.listen())
                .replaceWithVoid();
    }

    @Override
    public Uni<Void> asyncStop() {
        return httpServer.close();
    }

    public <T> Multi<CompletableInteraction<T>> on() {
        return processor.flatMap(context -> {
            Interaction<Interaction.Data> interaction = context.get("interaction");
            // validate the interaction with the schema

            // the schema allows you to pick the type (required)
            // from the type, cast the data
            // construct the appropriate completableRequest
        });
    }

    static class UnauthorizedException extends RuntimeException {
    }
}
