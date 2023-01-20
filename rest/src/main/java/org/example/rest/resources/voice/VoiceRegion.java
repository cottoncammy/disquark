package org.example.rest.resources.voice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.example.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ImmutableVoiceRegion.class)
public interface VoiceRegion {

    static Builder builder() {
        return new Builder();
    }

    String id();

    String name();

    boolean optimal();

    boolean deprecated();

    boolean custom();

    class Builder extends ImmutableVoiceRegion.Builder {
        protected Builder() {
        }
    }
}
