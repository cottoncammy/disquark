package org.example.rest.resources.oauth2;

import org.example.rest.immutables.ImmutableJson;
import org.example.rest.oauth2.Scope;
import org.example.rest.resources.Application;
import org.example.rest.resources.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ImmutableJson
public interface Authorization {

    static Builder builder() {
        return new Builder();
    }

    Application application();

    List<Scope> scopes();

    Instant expires();

    Optional<User> user();

    class Builder extends ImmutableAuthorization.Builder {
        protected Builder() {}
    }
}
