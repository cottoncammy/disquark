package io.disquark.rest.json.automod;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyAutoModerationRule extends AbstractRequestUni<AutoModerationRule> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake autoModerationRuleId();

    public abstract Optional<String> name();

    @JsonProperty("event_type")
    public abstract Optional<AutoModerationRule.EventType> eventType();

    @JsonProperty("trigger_metadata")
    public abstract Optional<AutoModerationRule.TriggerMetadata> triggerMetadata();

    public abstract Optional<List<AutoModerationAction>> actions();

    public abstract Optional<Boolean> enabled();

    @JsonProperty("exempt_roles")
    public abstract Optional<List<Snowflake>> exemptRoles();

    @JsonProperty("exempt_channels")
    public abstract Optional<List<Snowflake>> exemptChannels();

    @Override
    public void subscribe(UniSubscriber<? super AutoModerationRule> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(AutoModerationRule.class))
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(
                        Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/auto-moderation/rules/{auto_moderation_rule.id}"))
                .variables(variables("guild.id", guildId().getValue(), "auto_moderation_rule.id",
                        autoModerationRuleId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
