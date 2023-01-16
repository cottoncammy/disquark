package org.example.rest.resources.auditlog;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.scheduledevent.GuildScheduledEvent;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.user.User;
import org.example.rest.resources.webhook.Webhook;
import org.example.rest.resources.automod.AutoModerationRule;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.application.command.ApplicationCommand;
import org.immutables.value.Value.Enclosing;

import java.util.List;
import java.util.Optional;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableAuditLog.class)
public interface AuditLog {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("application_commands")
    List<ApplicationCommand> applicationCommands();

    @JsonProperty("audit_log_entries")
    List<Entry> auditLogEntries();

    @JsonProperty("auto_moderation_rules")
    List<AutoModerationRule> autoModerationRules();

    @JsonProperty("guild_scheduled_events")
    List<GuildScheduledEvent> guildScheduledEvents();

    List<Guild.Integration> integrations();

    List<Channel> threads();

    List<User> users();

    List<Webhook> webhooks();

    @ImmutableJson
    @JsonDeserialize(as = ImmutableAuditLog.Entry.class)
    interface Entry {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("target_id")
        Optional<String> targetId();

        Optional<List<Change>> changes();

        @JsonProperty("user_id")
        Optional<Snowflake> userId();

        Snowflake id();

        @JsonProperty("action_type")
        Event actionType();

        Optional<AuditEntryInfo> options();

        Optional<String> reason();

        class Builder extends ImmutableAuditLog.Entry.Builder {
            protected Builder() {}
        }
    }

    enum Event {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        GUILD_UPDATE(1),
        CHANNEL_CREATE(10),
        CHANNEL_UPDATE(11),
        CHANNEL_DELETE(12),
        CHANNEL_OVERWRITE_CREATE(13),
        CHANNEL_OVERWRITE_UPDATE(14),
        CHANNEL_OVERWRITE_DELETE(15),
        MEMBER_KICK(20),
        MEMBER_PRUNE(21),
        MEMBER_BAN_ADD(22),
        MEMBER_BAN_REMOVE(23),
        MEMBER_UPDATE(24),
        MEMBER_ROLE_UPDATE(25),
        MEMBER_MOVE(26),
        MEMBER_DISCONNECT(27),
        BOT_ADD(28),
        ROLE_CREATE(30),
        ROLE_UPDATE(31),
        ROLE_DELETE(32),
        INVITE_CREATE(40),
        INVITE_UPDATE(41),
        INVITE_DELETE(42),
        WEBHOOK_CREATE(50),
        WEBHOOK_UPDATE(51),
        WEBHOOK_DELETE(52),
        EMOJI_CREATE(60),
        EMOJI_UPDATE(61),
        EMOJI_DELETE(62),
        MESSAGE_DELETE(72),
        MESSAGE_BULK_DELETE(73),
        MESSAGE_PIN(74),
        MESSAGE_UNPIN(75),
        INTEGRATION_CREATE(80),
        INTEGRATION_UPDATE(81),
        INTEGRATION_DELETE(82),
        STAGE_INSTANCE_CREATE(83),
        STAGE_INSTANCE_UPDATE(84),
        STAGE_INSTANCE_DELETE(85),
        STICKER_CREATE(90),
        STICKER_UPDATE(91),
        STICKER_DELETE(92),
        GUILD_SCHEDULED_EVENT_CREATE(100),
        GUILD_SCHEDULED_EVENT_UPDATE(101),
        GUILD_SCHEDULED_EVENT_DELETE(102),
        THREAD_CREATE(110),
        THREAD_UPDATE(111),
        THREAD_DELETE(112),
        APPLICATION_COMMAND_PERMISSION_UPDATE(121),
        AUTO_MODERATION_RULE_CREATE(140),
        AUTO_MODERATION_RULE_UPDATE(141),
        AUTO_MODERATION_RULE_DELETE(142),
        AUTO_MODERATION_BLOCK_MESSAGE(143),
        AUTO_MODERATION_FLAG_TO_CHANNEL(144),
        AUTO_MODERATION_USER_COMMUNICATION_DISABLED(145);

        private final int value;

        Event(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableAuditLog.Change.class)
    interface Change {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("new_value")
        Optional<JsonNode> newValue();

        @JsonProperty("old_value")
        Optional<JsonNode> oldValue();

        String key();

        class Builder extends ImmutableAuditLog.Change.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableAuditLog.Builder {
        protected Builder() {}
    }
}
