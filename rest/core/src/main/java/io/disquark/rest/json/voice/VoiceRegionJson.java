package io.disquark.rest.json.voice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = VoiceRegion.class)
interface VoiceRegionJson {

    String id();

    String name();

    boolean optimal();

    boolean deprecated();

    boolean custom();
}
