package io.disquark.rest.resources.oauth2;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.application.Application;
import io.disquark.rest.resources.user.User;

@ImmutableJson
@JsonDeserialize(as = Authorization.class)
interface AuthorizationJson {

    Application application();

    List<Scope> scopes();

    Instant expires();

    Optional<User> user();
}
