package io.disquark.rest.json.sticker;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = StickerPack.class)
interface StickerPackJson {

    Snowflake id();

    List<Sticker> stickers();

    String name();

    @JsonProperty("sku_id")
    Snowflake skuId();

    @JsonProperty("cover_sticker_id")
    Optional<Snowflake> coverStickerId();

    String description();

    @JsonProperty("banner_asset_id")
    Optional<Snowflake> bannerAssetId();
}
