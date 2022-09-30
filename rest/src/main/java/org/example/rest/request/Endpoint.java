package org.example.rest.request;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.UriTemplate;
import io.vertx.mutiny.uritemplate.Variables;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class Endpoint {
    private final HttpMethod httpMethod;
    private final UriTemplate uriTemplate;
    private final boolean globallyRateLimited;
    private final boolean requiresAuthentication;

    public static Endpoint create(HttpMethod httpMethod, String uri, boolean globallyRateLimited, boolean requiresAuthentication) {
        return new Endpoint(requireNonNull(httpMethod), UriTemplate.of(requireNonNull(uri)), globallyRateLimited, requiresAuthentication);
    }

    public static Endpoint create(HttpMethod httpMethod, String uri) {
        return create(httpMethod, uri, true, true);
    }

    private Endpoint(
            HttpMethod httpMethod,
            UriTemplate uriTemplate,
            boolean globallyRateLimited,
            boolean requiresAuthentication) {
        this.httpMethod = httpMethod;
        this.uriTemplate = uriTemplate;
        this.globallyRateLimited = globallyRateLimited;
        this.requiresAuthentication = requiresAuthentication;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public UriTemplate getUriTemplate() {
        return uriTemplate;
    }

    public boolean isGloballyRateLimited() {
        return globallyRateLimited;
    }

    public boolean isAuthenticationRequired() {
        return requiresAuthentication;
    }

    public Optional<String> getTopLevelResourceValue(Variables variables) {
        return Optional.ofNullable(variables.getSingle("channel.id"))
                .or(() -> Optional.ofNullable(variables.getSingle("guild.id")))
                .or(() -> Optional.ofNullable(variables.getSingle("webhook.id")));
    }
}
