package org.example.rest.interactions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.core.buffer.Buffer;
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
    private final String routePath;
    private final HttpServer httpServer;
    private final InteractionValidator interactionValidator;
    private final DiscordInteractionsClient<?> interactionsClient;
    private final BroadcastProcessor<RoutingContext> processor = BroadcastProcessor.create();

    public InteractionsVerticle(
            Router router,
            Codec jsonCodec,
            String routePath,
            HttpServer httpServer,
            InteractionValidator interactionValidator,
            DiscordInteractionsClient<?> interactionsClient) {
        this.router = router;
        this.jsonCodec = jsonCodec;
        this.routePath = routePath;
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

                // TODO get body async
                Buffer body = request.bodyAndAwait();
                if (!interactionValidator.validate(timestamp, body.toString(), signature)) {
                    context.fail(401);
                }

                Interaction<Interaction.Data> interaction = jsonCodec.deserialize(body, Interaction.class);
                if (interaction.type() == Interaction.Type.PING) {
                }

                context.put("interaction", interaction);
                processor.onNext(context);
            }
        };
    }

    private Consumer<RoutingContext> failureHandler() {
        return new Consumer<RoutingContext>() {
            @Override
            public void accept(RoutingContext context) {
                if (context.statusCode() == 500) {
                    context.fail(400);
                }
            }
        };
    }

    @Override
    public Uni<Void> asyncStart() {
        return Uni.createFrom().item(httpServer)
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
}
