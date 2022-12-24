package org.example.rest.resources.guild.prune;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface BeginGuildPrune extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    static BeginGuildPrune create(Snowflake guildId) {
        return null;
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
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableBeginGuildPrune.Builder {
        protected Builder() {}
    }
}
