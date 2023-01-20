package org.example.rest.resources.guild.prune;

import static org.example.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

@ImmutableJson
public interface BeginGuildPrune extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    static BeginGuildPrune create(Snowflake guildId) {
        return ImmutableBeginGuildPrune.create(guildId);
    }

    @JsonIgnore
    Snowflake guildId();

    OptionalInt days();

    @JsonProperty("compute_prune_count")
    Optional<Boolean> computePruneCount();

    @JsonProperty("include_roles")
    Optional<List<Snowflake>> includeRoles();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/prune"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableBeginGuildPrune.Builder {
        protected Builder() {}
    }
}
