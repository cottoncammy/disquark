package org.example.rest.resources.interactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.User;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.channel.message.AllowedMentions;
import org.example.rest.resources.channel.message.Message;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.interactions.components.Component;
import org.example.rest.resources.interactions.components.SelectOption;
import org.example.rest.resources.permissions.Role;
import org.immutables.value.Value.Enclosing;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableInteraction.class)
public interface Interaction {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    @JsonProperty("application_id")
    Snowflake applicationId();

    Type type();

    Optional<Data> data();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    Optional<Guild.Member> member();

    Optional<User> user();

    String token();

    int version();

    Optional<Message> message();

    @JsonProperty("app_permissions")
    Optional<String> appPermissions();

    Optional<Locale> locale();

    @JsonProperty("guild_locale")
    Optional<Locale> guildLocale();

    enum Type {
        PING(1),
        APPLICATION_COMMAND(2),
        MESSAGE_COMPONENT(3),
        APPLICATION_COMMAND_AUTOCOMPLETE(4),
        MODAL_SUBMIT(5);

        private final int value;

        public static Type create(int value) {
            switch (value) {
                case 1:
                    return PING;
                case 2:
                    return APPLICATION_COMMAND;
                case 3:
                    return MESSAGE_COMPONENT;
                case 4:
                    return APPLICATION_COMMAND_AUTOCOMPLETE;
                case 5:
                    return MODAL_SUBMIT;
                default:
                    throw new IllegalArgumentException();
            }
        }

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableInteraction.Data.class)
    interface Data {

        static Builder builder() {
            return new Builder();
        }

        Optional<Snowflake> id();

        Optional<String> name();

        Optional<ApplicationCommand.Type> type();

        Optional<ResolvedData> resolved();

        Optional<List<ApplicationCommand.InteractionDataOption>> options();

        @JsonProperty("guild_id")
        Optional<Snowflake> guildId();

        @JsonProperty("target_id")
        Optional<Snowflake> targetId();

        @JsonProperty("custom_id")
        Optional<String> customId();

        @JsonProperty("component_type")
        Optional<Component.Type> componentType();

        Optional<List<String>> values();

        Optional<List<Component>> components();

        class Builder extends ImmutableInteraction.Data.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableInteraction.ResolvedData.class)
    interface ResolvedData {

        static Builder builder() {
            return new Builder();
        }

        Optional<Map<Snowflake, User>> users();

        Optional<Map<Snowflake, Guild.Member>> members();

        Optional<Map<Snowflake, Role>> roles();

        Optional<Map<Snowflake, Channel>> channels();

        Optional<Map<Snowflake, Message>> messages();

        Optional<Map<Snowflake, Message.Attachment>> attachments();

        class Builder extends ImmutableInteraction.ResolvedData.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableInteraction.Response.class)
    interface Response {

        static Builder builder() {
            return new Builder();
        }

        CallbackType type();

        Optional<CallbackData> data();

        class Builder extends ImmutableInteraction.Response.Builder {
            protected Builder() {}
        }
    }

    enum CallbackType {
        PONG(1),
        CHANNEL_MESSAGE_WITH_SOURCE(4),
        DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE(5),
        DEFERRED_UPDATE_MESSAGE(6),
        UPDATE_MESSAGE(7),
        APPLICATION_COMMAND_AUTOCOMPLETE_RESULT(8),
        MODAL(9);

        private final int value;

        public static CallbackType create(int value) {
            switch (value) {
                case 1:
                    return PONG;
                case 4:
                    return CHANNEL_MESSAGE_WITH_SOURCE;
                case 5:
                    return DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE;
                case 6:
                    return DEFERRED_UPDATE_MESSAGE;
                case 7:
                    return UPDATE_MESSAGE;
                case 8:
                    return APPLICATION_COMMAND_AUTOCOMPLETE_RESULT;
                case 9:
                    return MODAL;
                default:
                    throw new IllegalArgumentException();
            }
        }

        CallbackType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableInteraction.CallbackData.class)
    interface CallbackData {

        static Builder builder() {
            return new Builder();
        }

        Optional<Boolean> tts();

        Optional<String> content();

        Optional<List<Message.Embed>> embeds();

        @JsonProperty("allowed_mentions")
        Optional<AllowedMentions> allowedMentions();

        Optional<EnumSet<Message.Flag>> flags();

        Optional<List<Component>> components();

        Optional<List<Message.Attachment>> attachments();

        Optional<List<ApplicationCommand.Option.Choice>> choices();

        @JsonProperty("custom_id")
        Optional<String> customId();

        Optional<String> title();

        class Builder extends ImmutableInteraction.CallbackData.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableInteraction.Builder {
        protected Builder() {}
    }
}
