package org.example.rest.resources.automod;

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

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface ModifyAutoModerationRule extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    @JsonIgnore
    Snowflake autoModerationRuleId();

    Optional<String> name();

    @JsonProperty("event_type")
    Optional<AutoModerationRule.EventType> eventType();

    @JsonProperty("trigger_metadata")
    Optional<AutoModerationRule.TriggerMetadata> triggerMetadata();

    Optional<List<AutoModerationAction>> actions();

    Optional<Boolean> enabled();

    @JsonProperty("exempt_roles")
    Optional<List<Snowflake>> exemptRoles();

    @JsonProperty("exempt_channels")
    Optional<List<Snowflake>> exemptChannels();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()).set("auto_moderation_rule.id", autoModerationRuleId().getValueAsString()))
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyAutoModerationRule.Builder {
        protected Builder() {}
    }
}
