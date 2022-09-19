package org.example.rest.resources.voice;

import org.example.rest.immutables.ImmutableJson;

@ImmutableJson
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
        protected Builder() {}
    }
}
