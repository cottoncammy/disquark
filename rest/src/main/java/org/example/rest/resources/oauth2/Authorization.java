package org.example.rest.resources.oauth2;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;
import org.example.rest.jackson.ScopesDeserializer;
import org.example.rest.resources.application.Application;
import org.example.rest.resources.user.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ImmutableJson
@JsonDeserialize(as = ImmutableAuthorization.class)
public interface Authorization {

    static Builder builder() {
        return new Builder();
    }

    Application application();

    @JsonDeserialize(using = ScopesDeserializer.class)
    List<Scope> scopes();

    Instant expires();

    Optional<User> user();

    class Builder extends ImmutableAuthorization.Builder {
        protected Builder() {}
    }
}
