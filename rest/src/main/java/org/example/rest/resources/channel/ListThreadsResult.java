package org.example.rest.resources.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.channel.thread.ThreadMember;

import java.util.List;
import java.util.Optional;

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
        protected Builder() {}
    }
}
