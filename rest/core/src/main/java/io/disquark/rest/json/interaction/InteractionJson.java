package io.disquark.rest.json.interaction;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Locale;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.channel.Channel;
import io.disquark.rest.json.command.ApplicationCommand;
import io.disquark.rest.json.member.GuildMember;
import io.disquark.rest.json.message.AllowedMentions;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.message.MessageEmbed;
import io.disquark.rest.json.message.PartialAttachment;
import io.disquark.rest.json.messagecomponent.Component;
import io.disquark.rest.json.role.Role;
import io.disquark.rest.json.thread.ThreadMetadata;
import io.disquark.rest.json.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Interaction.class)
interface InteractionJson<T> {

    Snowflake id();

    @JsonProperty("application_id")
    Snowflake applicationId();

    Type type();

    Optional<T> data();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    Optional<GuildMember> member();

    Optional<User> user();

    String token();

    int version();

    Optional<Message> message();

    @JsonProperty("app_permissions")
    Optional<EnumSet<PermissionFlag>> appPermissions();

    Optional<Locale> locale();

    @JsonProperty("guild_locale")
    Optional<Locale> guildLocale();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        PING(1),
        APPLICATION_COMMAND(2),
        MESSAGE_COMPONENT(3),
        APPLICATION_COMMAND_AUTOCOMPLETE(4),
        MODAL_SUBMIT(5);

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
    @JsonDeserialize(as = Interaction.Data.class)
    interface DataJson {

        Optional<Snowflake> id();

        Optional<String> name();

        Optional<ApplicationCommand.Type> type();

        Optional<Interaction.ResolvedData> resolved();

        Optional<List<ApplicationCommandInteractionDataOption>> options();

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
    }

    @ImmutableJson
    @JsonDeserialize(as = Interaction.ApplicationCommandData.class)
    interface ApplicationCommandDataJson {

        Snowflake id();

        String name();

        ApplicationCommand.Type type();

        Optional<Interaction.ResolvedData> resolved();

        Optional<List<ApplicationCommandInteractionDataOption>> options();

        @JsonProperty("guild_id")
        Optional<Snowflake> guildId();

        @JsonProperty("target_id")
        Optional<Snowflake> targetId();
    }

    @ImmutableJson
    @JsonDeserialize(as = Interaction.MessageComponentData.class)
    interface MessageComponentDataJson {

        @JsonProperty("custom_id")
        String customId();

        @JsonProperty("component_type")
        Component.Type componentType();

        Optional<Interaction.ResolvedData> resolved();

        List<String> values();
    }

    @ImmutableJson
    @JsonDeserialize(as = Interaction.ModalSubmitData.class)
    interface ModalSubmitDataJson {

        @JsonProperty("custom_id")
        String customId();

        List<Component> components();
    }

    @ImmutableJson
    @JsonDeserialize(as = Interaction.ResolvedData.class)
    interface ResolvedDataJson {

        Optional<Map<Snowflake, User>> users();

        Optional<Map<Snowflake, Interaction.PartialGuildMember>> members();

        Optional<Map<Snowflake, Role>> roles();

        Optional<Map<Snowflake, Interaction.PartialChannel>> channels();

        Optional<Map<Snowflake, Message>> messages();

        Optional<Map<Snowflake, Message.Attachment>> attachments();

        @ImmutableJson
        @JsonDeserialize(as = Interaction.PartialGuildMember.class)
        interface PartialGuildMemberJson {

            Optional<String> nick();

            Optional<String> avatar();

            List<Snowflake> roles();

            @JsonProperty("joined_at")
            Instant joinedAt();

            @JsonProperty("premium_since")
            Optional<Instant> premiumSince();

            Optional<Boolean> pending();

            Optional<EnumSet<PermissionFlag>> permissions();

            @JsonProperty("communication_disabled_until")
            Optional<Instant> communicationDisabledUntil();
        }

        @ImmutableJson
        @JsonDeserialize(as = Interaction.PartialChannel.class)
        interface PartialChannelJson {

            Snowflake id();

            Channel.Type type();

            Optional<String> name();

            @JsonProperty("parent_id")
            Optional<Snowflake> parentId();

            @JsonProperty("thread_metadata")
            Optional<ThreadMetadata> threadMetadata();

            Optional<EnumSet<PermissionFlag>> permissions();
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = Interaction.Response.class)
    interface ResponseJson<T> {

        CallbackType type();

        Optional<T> data();
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

        CallbackType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = Interaction.CallbackData.class)
    interface CallbackDataJson {

        Optional<Boolean> tts();

        Optional<String> content();

        Optional<List<MessageEmbed>> embeds();

        @JsonProperty("allowed_mentions")
        Optional<AllowedMentions> allowedMentions();

        Optional<EnumSet<Message.Flag>> flags();

        Optional<List<Component>> components();

        Optional<List<PartialAttachment>> attachments();

        Optional<List<ApplicationCommand.OptionChoice>> choices();

        @JsonProperty("custom_id")
        Optional<String> customId();

        Optional<String> title();
    }

    @ImmutableJson
    @JsonDeserialize(as = Interaction.CallbackData.class)
    interface MessageCallbackDataJson {

        Optional<Boolean> tts();

        Optional<String> content();

        Optional<List<MessageEmbed>> embeds();

        @JsonProperty("allowed_mentions")
        Optional<AllowedMentions> allowedMentions();

        Optional<EnumSet<Message.Flag>> flags();

        Optional<List<Component>> components();
    }
}
