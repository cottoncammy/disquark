package org.example.rest.interactions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.core.http.HttpServerRequest;
import io.vertx.mutiny.ext.web.Route;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import io.vertx.mutiny.ext.web.handler.ResponseContentTypeHandler;
import org.example.rest.interactions.dsl.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;

import java.util.function.Consumer;

class InteractionsVerticle extends AbstractVerticle {
    private final Router router;
    private final HttpServer httpServer;
    private final String interactionsUrl;
    private final InteractionValidator interactionValidator;
    private final DiscordInteractionsClient<?> interactionsClient;
    private final BroadcastProcessor<RoutingContext> processor = BroadcastProcessor.create();

    public InteractionsVerticle(
            Router router,
            HttpServer httpServer,
            String interactionsUrl,
            InteractionValidator interactionValidator,
            DiscordInteractionsClient<?> interactionsClient) {
        this.router = router;
        this.httpServer = httpServer;
        this.interactionsUrl = interactionsUrl;
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
                            if (!interactionValidator.validate(Buffer.buffer(timestamp), body, Buffer.buffer(signature))) {
                                return Uni.createFrom().failure(UnauthorizedException::new);
                            }
                            return Uni.createFrom().voidItem();
                        })
                        .onFailure(UnauthorizedException.class).invoke(() -> context.fail(401))
                        .map(body -> body.toJsonObject().mapTo(Interaction.class))
                        .onFailure(IllegalArgumentException.class).invoke(() -> context.fail(400))
                        .call(interaction -> {
                            if (interaction.type() == Interaction.Type.PING) {
                                return new PingInteraction(interaction, context.response(), interactionsClient).pong();
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
        Route route = router.route(HttpMethod.POST, interactionsUrl)
                .consumes("application/json")
                .produces("application/json")
                .handler(requestHandler())
                .handler(ResponseContentTypeHandler.create());

        return Uni.createFrom().item(route)
                .replaceWith(httpServer.requestHandler(router))
                .call(() -> httpServer.listen())
                .replaceWithVoid();
    }

    @Override
    public Uni<Void> asyncStop() {
        return httpServer.close();
    }

    public <D, C extends CompletableInteraction<D>> Multi<C> on(InteractionSchema<D, C> schema) {
        return processor.onItem().transformToMultiAndMerge(context -> {
            Interaction<D> interaction = context.get("interaction");
            if (!schema.validate(interaction)) {
                return Uni.createFrom().voidItem();
            }

            return Uni.createFrom().item(schema.getCompletableInteraction(interaction, context.response(), interactionsClient));
        });
    }

    static class UnauthorizedException extends RuntimeException {
    }
}
