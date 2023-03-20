package io.disquark.rest.json.oauth2;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.application.Application;
import io.disquark.rest.json.user.User;

@ImmutableJson
@JsonDeserialize(as = Authorization.class)
interface AuthorizationJson {

    Application application();

    List<Scope> scopes();

    Instant expires();

    Optional<User> user();
}
