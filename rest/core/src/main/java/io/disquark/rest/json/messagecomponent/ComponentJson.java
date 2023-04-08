package io.disquark.rest.json.messagecomponent;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.json.emoji.Emoji;

@ImmutableJson
@JsonDeserialize(as = Component.class)
interface ComponentJson {

    Component.Type type();

    Optional<List<Component>> components();

    OptionalInt style();

    Optional<String> label();

    Optional<Emoji> emoji();

    Optional<String> customId();

    Optional<String> url();

    Optional<Boolean> disabled();

    Optional<List<SelectOption>> options();

    @JsonProperty("channel_types")
    Optional<List<Channel.Type>> channelTypes();

    Optional<String> placeholder();

    @JsonProperty("min_values")
    OptionalInt minValues();

    @JsonProperty("max_values")
    OptionalInt maxValues();

    @JsonProperty("min_length")
    OptionalInt minLength();

    @JsonProperty("max_length")
    OptionalInt maxLength();

    Optional<Boolean> required();

    Optional<String> value();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        ACTION_ROW(1),
        BUTTON(2),
        STRING_MENU(3),
        TEXT_INPUT(4),
        USER_SELECT(5),
        ROLE_SELECT(6),
        MENTIONABLE_SELECT(7),
        CHANNEL_SELECT(8);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }
}
