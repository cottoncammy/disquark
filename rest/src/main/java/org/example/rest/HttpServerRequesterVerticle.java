package org.example.rest;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.core.http.HttpServer;
import org.example.rest.request.Requester;
import org.example.rest.response.HttpResponse;

// TODO
public class HttpServerRequesterVerticle extends AbstractVerticle {
    private final HttpServer httpServer;
    private final Requester<HttpResponse> requester;

    private HttpServerRequesterVerticle(HttpServer httpServer, Requester<HttpResponse> requester) {
        this.httpServer = httpServer;
        this.requester = requester;
    }

    @Override
    public Uni<Void> asyncStart() {
        httpServer.requestHandler(req -> {

        });
        return httpServer.listen().replaceWithVoid();
    }

    @Override
    public Uni<Void> asyncStop() {
        return httpServer.close();
    }
}
