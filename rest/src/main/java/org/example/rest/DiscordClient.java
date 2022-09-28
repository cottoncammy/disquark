package org.example.rest;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.request.Requester;
import org.example.rest.response.Response;

public abstract class DiscordClient {
    protected final Vertx vertx;
    protected final Requester requester;

    protected DiscordClient(Vertx vertx, Requester requester) {
        this.vertx = vertx;
        this.requester = requester;
    }

    protected abstract static class Builder<T extends DiscordClient> {
        protected Vertx vertx;
        protected Requester requester;

        protected Builder() {}

        public Builder<T> vertx(Vertx vertx) {
            this.vertx = vertx;
            return this;
        }

        public Builder<T> requester(Requester requester) {
            this.requester = requester;
            return this;
        }

        public abstract T build();
    }
}
