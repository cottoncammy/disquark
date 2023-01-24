package io.disquark.rest.interactions;

import static io.disquark.rest.util.ExceptionPredicate.is;
import static io.disquark.rest.util.ExceptionPredicate.isNot;
import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import io.disquark.rest.interactions.dsl.InteractionSchema;
import io.disquark.rest.resources.interactions.Interaction;
import io.smallrye.mutiny.Context;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.UnicastProcessor;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.DecodeException;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.core.http.HttpServerRequest;
import io.vertx.mutiny.ext.web.Route;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import io.vertx.mutiny.ext.web.handler.CorsHandler;
import io.vertx.mutiny.ext.web.handler.ResponseContentTypeHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class InteractionsVerticle extends AbstractVerticle {
    private static final String REQUEST_ID = "request-id";
    private static final Logger LOG = LoggerFactory.getLogger(InteractionsVerticle.class);

    private final Router router;
    private final HttpServer httpServer;
    private final String interactionsUrl;
    private final InteractionValidator interactionValidator;
    private final DiscordInteractionsClient<?> interactionsClient;
    private final UnicastProcessor<RoutingContext> processor = UnicastProcessor.create();

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

    @SuppressWarnings("unchecked")
    private Uni<?> handleRequest(RoutingContext context, Context ctx) {
        HttpServerRequest request = context.request();
        if (LOG.isDebugEnabled()) {
            ctx.put(REQUEST_ID, Integer.toHexString(request.hashCode()));
        }

        return request.body()
                .call(body -> {
                    LOG.debug("Received incoming request {} from {}:{} with body {}",
                            ctx.get(REQUEST_ID), request.remoteAddress().host(), request.remoteAddress().port(), body);

                    String signature = requireNonNull(request.getHeader("X-Signature-Ed25519"));
                    String timestamp = requireNonNull(request.getHeader("X-Signature-Timestamp"));
                    if (!interactionValidator.validate(timestamp, body.toString(), signature)) {
                        return Uni.createFrom().failure(UnauthorizedException::new);
                    }
                    return Uni.createFrom().voidItem();
                })
                .onFailure(is(NullPointerException.class, UnauthorizedException.class)).call(() -> {
                    LOG.debug("Interaction validation failed for incoming request {}", ctx.<Object> get(REQUEST_ID));
                    return context.response().setStatusCode(401).end();
                })
                .map(body -> body.toJsonObject().mapTo(Interaction.class))
                .onFailure(is(IllegalArgumentException.class, DecodeException.class)).call(e -> {
                    LOG.warn("Request body mapping failed for incoming request {}: {}",
                            ctx.get(REQUEST_ID), e.getMessage());

                    return context.response().setStatusCode(400).end();
                })
                .call(interaction -> {
                    LOG.debug("Incoming request {} mapped to incoming {} interaction {}",
                            ctx.get(REQUEST_ID), interaction.type(), interaction);

                    if (interaction.type() == Interaction.Type.PING) {
                        return new PingInteraction(interaction, context.response(), interactionsClient).pong();
                    }
                    return Uni.createFrom().voidItem();
                })
                .invoke(interaction -> {
                    context.put("interaction", interaction);
                    processor.onNext(context);
                })
                .onFailure(isNot(NullPointerException.class, UnauthorizedException.class,
                        IllegalArgumentException.class, DecodeException.class))
                .invoke(e -> LOG.warn("Encountered exception while handling incoming request {}: {}",
                        ctx.get(REQUEST_ID), e.getMessage()))
                .onFailure().recoverWithNull();
    }

    private Consumer<RoutingContext> requestHandler() {
        return new Consumer<>() {
            @Override
            public void accept(RoutingContext context) {
                Uni.createFrom().context(ctx -> handleRequest(context, ctx))
                        .subscribe()
                        .with(x -> {
                        });
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
