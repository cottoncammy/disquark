package org.example.rest.request;

import io.smallrye.mutiny.Uni;
import org.example.rest.resources.oauth2.AccessToken;

// TODO handle no-op auth
@FunctionalInterface
public interface AccessTokenSource {

    Uni<AccessToken> getToken();
}
