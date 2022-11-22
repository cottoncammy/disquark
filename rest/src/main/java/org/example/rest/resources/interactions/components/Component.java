package org.example.rest.resources.interactions.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Emoji;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.channel.Channel;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
@JsonDeserialize(as = ImmutableComponent.class)
public interface Component {

    static Builder builder() {
        return new Builder();
    }

    Type type();

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
        ACTION_ROW(1),
        BUTTON(2),
        SELECT_MENU(3),
        TEXT_INPUT(4),
        USER_SELECT(5),
        ROLE_SELECT(6),
        MENTIONABLE_SELECT(7),
        CHANNEL_SELECT(8);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    class Builder extends ImmutableComponent.Builder {
        protected Builder() {}
    }
}
