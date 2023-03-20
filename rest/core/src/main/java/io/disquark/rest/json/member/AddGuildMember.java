package io.disquark.rest.json.member;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

import org.immutables.value.Value.Redacted;

@ImmutableUni
abstract class AddGuildMember extends AbstractRequestUni<GuildMember> {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake userId();

    @Redacted
    @JsonProperty("access_token")
    public abstract String accessToken();

    public abstract Optional<String> nick();

    public abstract Optional<List<Snowflake>> roles();

    public abstract Optional<Boolean> mute();

    public abstract Optional<Boolean> deaf();

    @Override
    public void subscribe(UniSubscriber<? super GuildMember> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(GuildMember.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/guilds/{guild.id}/members/{user.id}"))
                .variables(variables("guild.id", guildId().getValue(), "user.id", userId().getValue()))
                .body(this)
                .build();
    }
}
