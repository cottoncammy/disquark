package org.example.rest.request.ratelimit;

import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.UriTemplate;
import org.example.rest.request.Endpoint;

public class BucketCacheKey {


    public static BucketCacheKey create(Endpoint endpoint, String expandedTemplate) {

    }

    private BucketCacheKey(HttpMethod httpMethod, UriTemplate uriTemplate, String topLevelResource) {
    }
}
