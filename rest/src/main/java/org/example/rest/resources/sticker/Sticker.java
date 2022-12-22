package org.example.rest.resources.sticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.ImmutableSticker;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.user.User;
import org.immutables.value.Value.Enclosing;

import java.util.List;
import java.util.Optional;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableSticker.class)
public interface Sticker {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    @JsonProperty("pack_id")
    Optional<Snowflake> packId();

    String name();

    Optional<String> description();

    String tags();

    @Deprecated
    Optional<String> asset();

    Type type();

    @JsonProperty("format_type")
    FormatType formatType();

    Optional<Boolean> available();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    Optional<User> user();

    @JsonProperty("sort_value")
    int sortValue();

    enum Type {
        STANDARD(1),
        GUILD(2);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum FormatType {
        PNG(1),
        APNG(2),
        LOTTIE(3);

        private final int value;

        FormatType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableSticker.Item.class)
    interface Item {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        String name();

        @JsonProperty("format_type")
        FormatType formatType();

        class Builder extends ImmutableSticker.Item.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableSticker.Pack.class)
    interface Pack {

        static Builder builder() {
            return new Builder();
        }

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

        class Builder extends ImmutableSticker.Pack.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableSticker.Builder {
        protected Builder() {}
    }
}
