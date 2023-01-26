package io.disquark.rest.resources.channel.thread;

import java.util.Optional;

import io.disquark.immutables.ImmutableBuilder;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.uritemplate.Variables;

import org.immutables.value.Value.Default;

@ImmutableBuilder
public interface ListThreadMembers extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    static ListThreadMembers create(Snowflake channelId) {
        return ImmutableListThreadMembers.create(channelId);
    }

    Snowflake channelId();

    @Default
    default boolean withMember() {
        return false;
    }

    Optional<Snowflake> after();

    @Default
    default int limit() {
        return 100;
    }

    @Override
    default Request asRequest() {
        JsonObject json = JsonObject.of("channel.id", channelId().getValue(), "with_member", withMember(), "limit", limit());
        if (after().isPresent()) {
            json.put("after", after().get().getValue());
        }

        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/channels/{channel.id}/thread-members{?with_member,after,limit}"))
                .variables(Variables.variables(json))
                .build();
    }

    class Builder extends ImmutableListThreadMembers.Builder {
        protected Builder() {
        }
    }
}