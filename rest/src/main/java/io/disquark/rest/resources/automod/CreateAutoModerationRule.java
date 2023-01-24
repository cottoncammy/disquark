package io.disquark.rest.resources.automod;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

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
        protected Builder() {
        }
    }
}
