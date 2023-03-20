package io.disquark.rest.json.thread;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.channel.Channel;

@ImmutableJson
@JsonDeserialize(as = ListThreadsResult.class)
interface ListThreadsResultJson {

    List<Channel> threads();

    List<ThreadMember> members();

    @JsonProperty("has_more")
    Optional<Boolean> hasMore();
}
