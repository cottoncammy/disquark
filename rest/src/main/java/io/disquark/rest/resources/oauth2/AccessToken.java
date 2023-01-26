package io.disquark.rest.resources.oauth2;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.jackson.ScopesDeserializer;
import io.disquark.rest.resources.guild.Guild;
import io.disquark.rest.resources.webhook.Webhook;

import org.immutables.value.Value.Redacted;

@ImmutableJson
@JsonDeserialize(as = ImmutableAccessToken.class)
public interface AccessToken {

    static Builder builder() {
        return new Builder();
    }

    @Redacted
    @JsonProperty("access_token")
    String accessToken();

    @JsonProperty("token_type")
    TokenType tokenType();

    @JsonProperty("expires_in")
    OptionalLong expiresIn();

    @JsonProperty("refresh_token")
    Optional<String> refreshToken();

    @JsonDeserialize(contentUsing = ScopesDeserializer.class)
    Optional<List<Scope>> scope();

    Optional<Guild> guild();

    Optional<Webhook> webhook();

    class Builder extends ImmutableAccessToken.Builder {
        protected Builder() {
        }
    }
}