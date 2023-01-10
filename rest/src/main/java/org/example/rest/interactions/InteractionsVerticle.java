package org.example.rest.interactions;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.DecodeException;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.core.http.HttpServerRequest;
import io.vertx.mutiny.core.http.HttpServerResponse;
import io.vertx.mutiny.core.net.SocketAddress;
import io.vertx.mutiny.ext.web.Route;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import io.vertx.mutiny.ext.web.handler.CorsHandler;
import io.vertx.mutiny.ext.web.handler.ResponseContentTypeHandler;
import org.example.rest.interactions.dsl.InteractionSchema;
import org.example.rest.resources.interactions.Interaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.ExceptionPredicate.is;
import static org.example.rest.util.ExceptionPredicate.isNot;

class InteractionsVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(InteractionsVerticle.class);
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
        return new Consumer<>() {
            @Override
            @SuppressWarnings("unchecked")
            public void accept(RoutingContext context) {
                HttpServerRequest request = context.request();
                String id = Integer.toHexString(request.hashCode());

                String signature = request.getHeader("X-Signature-Ed25519");
                String timestamp = request.getHeader("X-Signature-Timestamp");
                HttpServerResponse response = context.response();

                request.body()
                        .call(body -> {
                            SocketAddress address = request.remoteAddress();
                            LOG.debug("Received incoming request {} from {}:{} with body {}",
                                    id, address.host(), address.port(), body);

                            if (!interactionValidator.validate(requireNonNull(timestamp), body.toString(), requireNonNull(signature))) {
                                return Uni.createFrom().failure(UnauthorizedException::new);
                            }
                            return Uni.createFrom().voidItem();
                        })
                        .onFailure(is(NullPointerException.class, UnauthorizedException.class)).call(() -> {
                            LOG.debug("Interaction validation failed for incoming request {}", id);
                            return response.setStatusCode(401).end();
                        })
                        .map(body -> body.toJsonObject().mapTo(Interaction.class))
                        .onFailure(is(IllegalArgumentException.class, DecodeException.class)).call(e -> {
                            LOG.warn("Request body mapping failed for incoming request {}: {}", id, e.getMessage());
                            return response.setStatusCode(400).end();
                        })
                        .call(interaction -> {
                            if (interaction.type() == Interaction.Type.PING) {
                                return new PingInteraction(interaction, response, interactionsClient).pong();
                            }
                            return Uni.createFrom().voidItem();
                        })
                        .invoke(interaction -> {
                            LOG.debug("Incoming request {} mapped to {} interaction: {}",
                                    id, interaction.type(), interaction);

                            context.put("interaction", interaction);
                            processor.onNext(context);
                        })
                        .onFailure(isNot(NullPointerException.class, UnauthorizedException.class,
                                IllegalArgumentException.class, DecodeException.class))
                        .invoke(e -> LOG.warn("Encountered exception while handling incoming request {}: {}",
                                id, e.getMessage()))
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
                .handler(CorsHandler.create("discord\\.com"))
                .handler(requestHandler())
                .handler(ResponseContentTypeHandler.create());

        return Uni.createFrom().item(route)
                .replaceWith(httpServer.requestHandler(router))
                .invoke(() -> LOG.debug("Starting HTTP server on port {}", httpServer.actualPort()))
                .call(() -> httpServer.listen())
                .replaceWithVoid();
    }

    @Override
    public Uni<Void> asyncStop() {
        return Uni.createFrom().voidItem()
                .invoke(() -> LOG.debug("Shutting down HTTP server"))
                .replaceWith(httpServer.close());
    }

    public <D, C extends CompletableInteraction<D>> Multi<C> on(InteractionSchema<D, C> schema) {
        return processor.onItem().transformToUniAndMerge(context -> {
            Interaction<D> interaction = context.get("interaction");
            if (!schema.validate(interaction)) {
                return Uni.createFrom().nullItem();
            }
            return Uni.createFrom().item(schema.getCompletableInteraction(interaction, context.response(), interactionsClient));
        });
    }

    static class UnauthorizedException extends RuntimeException {
    }
}
