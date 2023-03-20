package io.disquark.rest.json.emoji;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
abstract class ModifyGuildEmoji extends AbstractRequestUni<Emoji> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake emojiId();

    public abstract Optional<String> name();

    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<List<Snowflake>> roles();

    @Override
    public void subscribe(UniSubscriber<? super Emoji> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Emoji.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}/emojis/{emoji.id}"))
                .variables(variables("guild.id", guildId().getValue(), "emoji.id", emojiId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
