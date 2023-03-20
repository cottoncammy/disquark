package io.disquark.rest.json.command;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.json.Locale;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.channel.Channel;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ApplicationCommand.class)
interface ApplicationCommandJson {

    Snowflake id();

    Optional<Type> type();

    @JsonProperty("application_id")
    Snowflake applicationId();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    String name();

    @JsonProperty("name_localizations")
    Optional<Map<Locale, String>> nameLocalizations();

    String description();

    @JsonProperty("description_localizations")
    Optional<Map<Locale, String>> descriptionLocalizations();

    Optional<List<ApplicationCommand.Option>> options();

    @JsonProperty("default_member_permissions")
    Optional<EnumSet<PermissionFlag>> defaultMemberPermissions();

    @JsonProperty("dm_permission")
    Optional<Boolean> dmPermission();

    @JsonProperty("default_permission")
    Optional<Boolean> defaultPermission();

    Optional<Boolean> nsfw();

    Snowflake version();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        CHAT_INPUT(1),
        USER(2),
        MESSAGE(3);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ApplicationCommand.Option.class)
    interface OptionJson {

        Type type();

        String name();

        @JsonProperty("name_localizations")
        @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
        NullableOptional<Map<Locale, String>> nameLocalizations();

        String description();

        @JsonProperty("description_localizations")
        @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
        NullableOptional<Map<Locale, String>> descriptionLocalizations();

        Optional<Boolean> required();

        Optional<List<ApplicationCommand.OptionChoice>> choices();

        Optional<List<ApplicationCommand.Option>> options();

        @JsonProperty("channel_types")
        Optional<List<Channel.Type>> channelTypes();

        @JsonProperty("min_value")
        OptionalDouble minValue();

        @JsonProperty("max_value")
        OptionalDouble maxValue();

        @JsonProperty("min_length")
        OptionalInt minLength();

        @JsonProperty("max_length")
        OptionalInt maxLength();

        Optional<Boolean> autocomplete();

        enum Type {
            @JsonEnumDefaultValue
            UNKNOWN(-1),
            SUB_COMMAND(1),
            SUB_COMMAND_GROUP(2),
            STRING(3),
            INTEGER(4),
            BOOLEAN(5),
            USER(6),
            CHANNEL(7),
            ROLE(8),
            MENTIONABLE(9),
            NUMBER(10),
            ATTACHMENT(11);

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

    @ImmutableJson
    @JsonDeserialize(as = ApplicationCommand.OptionChoice.class)
    interface OptionChoiceJson {

        String name();

        @JsonProperty("name_localizations")
        @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
        NullableOptional<Map<Locale, String>> nameLocalizations();

        JsonNode value();
    }
}
