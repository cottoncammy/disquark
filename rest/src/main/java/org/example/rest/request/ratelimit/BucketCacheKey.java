package org.example.rest.request.ratelimit;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.UriTemplate;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;

import javax.annotation.Nullable;
import java.util.Objects;

class BucketCacheKey {
    private final HttpMethod httpMethod;
    private final UriTemplate uriTemplate;
    @Nullable
    private final String topLevelResourceValue;

    public static BucketCacheKey create(Request request) {
        Endpoint endpoint = request.endpoint();
        return new BucketCacheKey(
                endpoint.getHttpMethod(),
                endpoint.getUriTemplate(),
                endpoint.getTopLevelResourceValue(request.variables()).get());
    }

    private BucketCacheKey(HttpMethod httpMethod, UriTemplate uriTemplate, String topLevelResourceValue) {
        this.httpMethod = httpMethod;
        this.uriTemplate = uriTemplate;
        this.topLevelResourceValue = topLevelResourceValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BucketCacheKey that = (BucketCacheKey) o;
        return httpMethod.equals(that.httpMethod) &&
                uriTemplate.equals(that.uriTemplate) &&
                Objects.equals(topLevelResourceValue, that.topLevelResourceValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, uriTemplate, topLevelResourceValue);
    }
}
