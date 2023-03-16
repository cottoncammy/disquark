package io.disquark.rest.resources.channel.thread;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ThreadMetadata.class)
interface ThreadMetadataJson {

    boolean archived();

    @JsonProperty("auto_archive_duration")
    int autoArchiveDuration();

    @JsonProperty("archive_timestamp")
    Instant archiveTimestamp();

    boolean locked();

    Optional<Boolean> invitable();

    @JsonProperty("create_timestamp")
    Optional<Instant> createTimestamp();
}
