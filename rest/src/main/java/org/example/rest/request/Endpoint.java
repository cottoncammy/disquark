package org.example.rest.request;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.UriTemplate;
import io.vertx.mutiny.uritemplate.Variables;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Endpoint {
    private final HttpMethod httpMethod;
    private final UriTemplate uriTemplate;

    public static Endpoint create(HttpMethod httpMethod, String uri) {
        return new Endpoint(requireNonNull(httpMethod), UriTemplate.of(requireNonNull(uri)));
    }

    private Endpoint(HttpMethod httpMethod, UriTemplate uriTemplate) {
        this.httpMethod = httpMethod;
        this.uriTemplate = uriTemplate;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public UriTemplate getUriTemplate() {
        return uriTemplate;
    }

    public Optional<String> getTopLevelResourceValue(Variables variables) {
        return Optional.ofNullable(variables.getSingle("channel.id"))
                .or(() -> Optional.ofNullable(variables.getSingle("guild.id")))
                .or(() -> Optional.ofNullable(variables.getSingle("webhook.id")));
    }
}
