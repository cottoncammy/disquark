package io.disquark.rest.json.sticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = StickerItem.class)
interface StickerItemJson {

    Snowflake id();

    String name();

    @JsonProperty("format_type")
    Sticker.FormatType formatType();
}
