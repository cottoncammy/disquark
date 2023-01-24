package io.disquark.rest.resources.channel;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.channel.thread.ThreadMember;

@ImmutableJson
@JsonDeserialize(as = ImmutableListThreadsResult.class)
public interface ListThreadsResult {

    static Builder builder() {
        return new Builder();
    }

    List<Channel> threads();

    List<ThreadMember> members();

    @JsonProperty("has_more")
    Optional<Boolean> hasMore();

    class Builder extends ImmutableListThreadsResult.Builder {
        protected Builder() {
        }
    }
}
