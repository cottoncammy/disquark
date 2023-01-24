package io.disquark.rest.resources.guild.prune;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

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
        protected Builder() {
        }
    }
}
