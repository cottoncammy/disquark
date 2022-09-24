package org.example.rest.request;

import io.smallrye.mutiny.Uni;
import org.example.rest.resources.oauth2.AccessToken;

@FunctionalInterface
public interface AccessTokenSource {

    Uni<AccessToken> getToken();
}
