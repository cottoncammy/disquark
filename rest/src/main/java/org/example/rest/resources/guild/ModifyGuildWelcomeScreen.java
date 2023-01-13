package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.nullableoptional.NullableOptional;

import java.util.List;
import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
public interface ModifyGuildWelcomeScreen extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonInclude(Include.NON_ABSENT)
    Optional<Boolean> enabled();

    @JsonProperty("welcome_channels")
    NullableOptional<List<Guild.WelcomeScreen.Channel>> welcomeChannels();

    NullableOptional<String> description();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/welcome-screen"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuildWelcomeScreen.Builder {
        protected Builder() {}
    }
}
