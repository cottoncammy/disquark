package io.disquark.rest.json.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class ModifyGuildWidget extends AbstractRequestUni<Guild.WidgetSettings> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract Optional<Boolean> enabled();

    @JsonProperty("channel_id")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Snowflake> channelId();

    @Override
    public void subscribe(UniSubscriber<? super Guild.WidgetSettings> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(Guild.WidgetSettings.class))
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/widget"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
