package io.disquark.rest.resources.application.command;

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
import io.disquark.rest.resources.Locale;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.Channel;
import io.disquark.rest.resources.permissions.PermissionFlag;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableApplicationCommand.class)
public interface ApplicationCommand {

    static Builder builder() {
        return new Builder();
    }

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

    Optional<List<Option>> options();

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
    @JsonDeserialize(as = ImmutableApplicationCommand.Option.class)
    interface Option {

        static Builder builder() {
            return new Builder();
        }

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

        Optional<List<Choice>> choices();

        Optional<List<Option>> options();

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

        @ImmutableJson
        @JsonDeserialize(as = ImmutableApplicationCommand.Choice.class)
        interface Choice {

            static Builder builder() {
                return new Builder();
            }

            String name();

            @JsonProperty("name_localizations")
            @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
            NullableOptional<Map<Locale, String>> nameLocalizations();

            JsonNode value();

            class Builder extends ImmutableApplicationCommand.Choice.Builder {
                protected Builder() {
                }
            }
        }

        class Builder extends ImmutableApplicationCommand.Option.Builder {
            protected Builder() {
            }
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableApplicationCommand.Permissions.class)
    interface Permissions {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        Type type();

        boolean permission();

        enum Type {
            @JsonEnumDefaultValue
            UNKNOWN(-1),
            ROLE(1),
            USER(2),
            CHANNEL(3);

            private final int value;

            Type(int value) {
                this.value = value;
            }

            @JsonValue
            public int getValue() {
                return value;
            }
        }

        class Builder extends ImmutableApplicationCommand.Permissions.Builder {
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableApplicationCommand.Builder {
        protected Builder() {
        }
    }
}
