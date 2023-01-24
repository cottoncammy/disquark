package io.disquark.rest.request.ratelimit;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import io.disquark.rest.request.Request;
import io.disquark.rest.util.UriTemplate;
import io.vertx.core.http.HttpMethod;

class BucketCacheKey {
    private final HttpMethod httpMethod;
    private final UriTemplate uriTemplate;
    @Nullable
    private final String topLevelResourceValue;

    public static BucketCacheKey create(Request request) {
        return new BucketCacheKey(
                request.endpoint().getHttpMethod(),
                request.endpoint().getUriTemplate(),
                request.endpoint().getTopLevelResourceValue(request.variables()).orElse(null));
    }

    private BucketCacheKey(HttpMethod httpMethod, UriTemplate uriTemplate, String topLevelResourceValue) {
        this.httpMethod = httpMethod;
        this.uriTemplate = uriTemplate;
        this.topLevelResourceValue = topLevelResourceValue;
    }

    public Optional<String> getTopLevelResourceValue() {
        return Optional.ofNullable(topLevelResourceValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BucketCacheKey that = (BucketCacheKey) o;
        return httpMethod.equals(that.httpMethod) &&
                uriTemplate.equals(that.uriTemplate) &&
                Objects.equals(topLevelResourceValue, that.topLevelResourceValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, uriTemplate, topLevelResourceValue);
    }

    @Override
    public String toString() {
        return "BucketCacheKey{" +
                "httpMethod=" + httpMethod + ", " +
                "uriTemplate=" + uriTemplate + ", " +
                "topLevelResourceValue=" + topLevelResourceValue +
                '}';
    }
}
