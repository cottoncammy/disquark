package io.disquark.rest.json.guild;

import static io.disquark.rest.util.Variables.variables;

import com.fasterxml.jackson.annotation.JsonCreator;
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
abstract class ModifyGuildMfaLevel extends AbstractRequestUni<Guild.MfaLevel> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract Guild.MfaLevel level();

    @Override
    public void subscribe(UniSubscriber<? super Guild.MfaLevel> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(Response.class))
                .map(Response::getLevel)
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/guilds/{guild.id}/mfa"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    static class Response {
        private final Guild.MfaLevel level;

        @JsonCreator
        public Response(@JsonProperty("level") Guild.MfaLevel level) {
            this.level = level;
        }

        public Guild.MfaLevel getLevel() {
            return level;
        }
    }
}
