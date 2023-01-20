package org.example.rest.resources.channel.thread;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ImmutableThreadMetadata.class)
public interface ThreadMetadata {

    static Builder builder() {
        return new Builder();
    }

    boolean archived();

    @JsonProperty("auto_archive_duration")
    int autoArchiveDuration();

    @JsonProperty("archive_timestamp")
    Instant archiveTimestamp();

    boolean locked();

    Optional<Boolean> invitable();

    @JsonProperty("create_timestamp")
    Optional<Instant> createTimestamp();

    class Builder extends ImmutableThreadMetadata.Builder {
        protected Builder() {
        }
    }
}
