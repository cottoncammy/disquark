package org.example.rest.resources.sticker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListNitroStickerPacksResponse {
    private final List<Sticker.Pack> stickerPacks;

    @JsonCreator
    public ListNitroStickerPacksResponse(@JsonProperty List<Sticker.Pack> stickerPacks) {
        this.stickerPacks = stickerPacks;
    }

    public List<Sticker.Pack> getStickerPacks() {
        return stickerPacks;
    }
}
