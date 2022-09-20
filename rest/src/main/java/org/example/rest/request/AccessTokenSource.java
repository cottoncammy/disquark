package org.example.rest.request;

import io.vertx.core.Future;
import org.example.rest.resources.oauth2.AccessToken;

@FunctionalInterface
public interface AccessTokenSource {

    Future<AccessToken> getToken();
}
