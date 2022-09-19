package org.example.rest.request;

import io.vertx.core.http.HttpMethod;
import io.vertx.uritemplate.UriTemplate;

public class Endpoint {
    private final HttpMethod httpMethod;
    private final UriTemplate uriTemplate;
    private final boolean globallyRateLimited;

    public static Endpoint of(HttpMethod httpMethod, String uri, boolean globallyRateLimited) {
        return new Endpoint(httpMethod, UriTemplate.of(uri), globallyRateLimited);
    }

    public static Endpoint of(HttpMethod httpMethod, String uri) {
        return of(httpMethod, uri, true);
    }

    private Endpoint(HttpMethod httpMethod, UriTemplate uriTemplate, boolean globallyRateLimited) {
        this.httpMethod = httpMethod;
        this.uriTemplate = uriTemplate;
        this.globallyRateLimited = globallyRateLimited;
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
}
