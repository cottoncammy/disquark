package org.example.rest.resources.application.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.permissions.PermissionFlag;
import org.immutables.value.Value.Enclosing;

import java.util.*;

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

    Snowflake guildId();

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

    Snowflake version();

    enum Type {
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
        Optional<Map<Locale, String>> nameLocalizations();

        String description();

        @JsonProperty("description_localizations")
        Optional<Map<Locale, String>> descriptionLocalizations();

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

        // TODO
        @ImmutableJson
        @JsonDeserialize(as = ImmutableApplicationCommand.Choice.class)
        interface Choice {

            static Builder builder() {
                return new Builder();
            }

            String name();

            @JsonProperty("name_localizations")
            Optional<Map<Locale, String>> nameLocalizations();

            Object value();

            class Builder extends ImmutableApplicationCommand.Choice.Builder {
                protected Builder() {}
            }
        }

        class Builder extends ImmutableApplicationCommand.Option.Builder {
            protected Builder() {}
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
            protected Builder() {}
        }
    }

    // TODO
    @ImmutableJson
    @JsonDeserialize(as = ImmutableApplicationCommand.InteractionDataOption.class)
    interface InteractionDataOption {

        static Builder builder() {
            return new Builder();
        }

        String name();

        Option.Type type();

        Optional<Object> value();

        Optional<List<Option>> options();

        Optional<Boolean> focused();

        class Builder extends ImmutableApplicationCommand.InteractionDataOption.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableApplicationCommand.Builder {
        protected Builder() {}
    }
}
