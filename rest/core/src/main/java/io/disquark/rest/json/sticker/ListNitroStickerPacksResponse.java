package io.disquark.rest.json.sticker;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListNitroStickerPacksResponse {
    private final List<StickerPack> stickerPacks;

    @JsonCreator
    public ListNitroStickerPacksResponse(@JsonProperty("sticker_packs") List<StickerPack> stickerPacks) {
        this.stickerPacks = stickerPacks;
    }

    public List<StickerPack> getStickerPacks() {
        return stickerPacks;
    }
}
