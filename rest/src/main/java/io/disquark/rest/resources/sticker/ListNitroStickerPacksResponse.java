package io.disquark.rest.resources.sticker;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListNitroStickerPacksResponse {
    private final List<Sticker.Pack> stickerPacks;

    @JsonCreator
    public ListNitroStickerPacksResponse(@JsonProperty("sticker_packs") List<Sticker.Pack> stickerPacks) {
        this.stickerPacks = stickerPacks;
    }

    public List<Sticker.Pack> getStickerPacks() {
        return stickerPacks;
    }
}
