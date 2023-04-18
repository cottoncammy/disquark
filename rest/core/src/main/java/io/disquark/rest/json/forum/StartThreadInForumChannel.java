package io.disquark.rest.json.forum;

import static io.disquark.rest.util.Variables.variables;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

// TODO returns a channel with a nested message
@ImmutableUni
abstract class StartThreadInForumChannel extends AbstractRequestUni<Channel> implements Auditable {

    @JsonIgnore
    public abstract Snowflake channelId();

    public abstract String name();

    @JsonProperty("auto_archive_duration")
    public abstract OptionalInt autoArchiveDuration();

    @JsonProperty("rate_limit_per_user")
    public abstract OptionalInt rateLimitPerUser();

    public abstract ForumThreadMessageParams message();

    @JsonProperty("applied_tags")
    public abstract Optional<List<Snowflake>> appliedTags();

    @Override
    public void subscribe(UniSubscriber<? super Channel> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Channel.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/channels/{channel.id}/threads"))
                .variables(variables("channel.id", channelId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .files(message().files())
                .build();
    }
}
