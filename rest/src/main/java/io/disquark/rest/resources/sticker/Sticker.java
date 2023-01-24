package io.disquark.rest.resources.sticker;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.user.User;

import org.immutables.value.Value.Enclosing;

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

    Type type();

    @JsonProperty("format_type")
    FormatType formatType();

    Optional<Boolean> available();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    Optional<User> user();

    @JsonProperty("sort_value")
    OptionalInt sortValue();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
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
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        PNG(1),
        APNG(2),
        LOTTIE(3),
        GIF(4);

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
            protected Builder() {
            }
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
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableSticker.Builder {
        protected Builder() {
        }
    }
}
