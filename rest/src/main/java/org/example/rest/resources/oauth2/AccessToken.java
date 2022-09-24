package org.example.rest.resources.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.Webhook;

import java.util.Optional;
import java.util.OptionalLong;

@ImmutableJson
public interface AccessToken {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("access_token")
    String accessToken();

    @JsonProperty("token_type")
    TokenType tokenType();

    @JsonProperty("expires_in")
    OptionalLong expiresIn();

    @JsonProperty("refresh_token")
    Optional<String> refreshToken();

    Optional<String> scope();

    Optional<Webhook> webhook();

    class Builder extends ImmutableAccessToken.Builder {
        protected Builder() {}
    }
}
