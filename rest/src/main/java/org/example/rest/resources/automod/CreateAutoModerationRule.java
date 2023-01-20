package org.example.rest.resources.automod;

import static org.example.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

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
public interface CreateAutoModerationRule extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    String name();

    @JsonProperty("event_type")
    AutoModerationRule.EventType eventType();

    @JsonProperty("trigger_type")
    AutoModerationRule.TriggerType triggerType();

    @JsonProperty("trigger_metadata")
    Optional<AutoModerationRule.TriggerMetadata> triggerMetadata();

    List<AutoModerationAction> actions();

    Optional<Boolean> enabled();

    @JsonProperty("exempt_roles")
    Optional<List<Snowflake>> exemptRoles();

    @JsonProperty("exempt_channels")
    Optional<List<Snowflake>> exemptChannels();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/auto-moderation/rules"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableCreateAutoModerationRule.Builder {
        protected Builder() {}
    }
}
