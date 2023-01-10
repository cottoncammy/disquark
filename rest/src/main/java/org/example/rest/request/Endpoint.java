package org.example.rest.request;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.UriTemplate;
import io.vertx.mutiny.uritemplate.Variables;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Endpoint {
    private final HttpMethod httpMethod;
    private final UriTemplate uriTemplate;
    private final boolean requiresAuthentication;

    public static Endpoint create(HttpMethod httpMethod, String uri, boolean requiresAuthentication) {
        return new Endpoint(requireNonNull(httpMethod, "httpMethod"), UriTemplate.of(requireNonNull(uri, "uri")), requiresAuthentication);
    }

    public static Endpoint create(HttpMethod httpMethod, String uri) {
        return create(httpMethod, uri, true);
    }

    private Endpoint(HttpMethod httpMethod, UriTemplate uriTemplate, boolean requiresAuthentication) {
        this.httpMethod = httpMethod;
        this.uriTemplate = uriTemplate;
        this.requiresAuthentication = requiresAuthentication;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public UriTemplate getUriTemplate() {
        return uriTemplate;
    }

    public boolean isAuthenticationRequired() {
        return requiresAuthentication;
    }

    public Optional<String> getTopLevelResourceValue(Variables variables) {
        String uri = uriTemplate.expandToString(Variables.variables());
        if (uri.startsWith("/channels/{channel.id}")) {
            return Optional.of(variables.getSingle("channel.id"));
        }

        if (uri.startsWith("/guilds/{guild.id}")) {
            return Optional.of(variables.getSingle("guild.id"));
        }

        if (uri.startsWith("/webhooks/{webhook.id}")) {
            return Optional.of(variables.getSingle("webhook.id"));
        }

        return Optional.empty();
    }
}
