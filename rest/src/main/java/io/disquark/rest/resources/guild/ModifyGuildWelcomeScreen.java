package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class ModifyGuildWelcomeScreen extends AbstractRequestUni<Guild.WelcomeScreen> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonInclude(Include.NON_ABSENT)
    public abstract Optional<Boolean> enabled();

    @JsonProperty("welcome_channels")
    public abstract NullableOptional<List<Guild.WelcomeScreenChannel>> welcomeChannels();

    public abstract NullableOptional<String> description();

    @Override
    public void subscribe(UniSubscriber<? super Guild.WelcomeScreen> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Guild.WelcomeScreen.class)).subscribe()
                .withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/welcome-screen"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
