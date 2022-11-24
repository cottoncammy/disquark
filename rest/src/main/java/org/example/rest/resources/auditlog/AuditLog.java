package org.example.rest.resources.auditlog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.GuildScheduledEvent;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.User;
import org.example.rest.resources.Webhook;
import org.example.rest.resources.automod.AutoModerationRule;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.interactions.ApplicationCommand;
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

    @JsonProperty("audit_moderation_rules")
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

        Optional<List<OptionalAuditEntryInfo>> options();

        Optional<String> reason();

        class Builder extends ImmutableAuditLog.Entry.Builder {
            protected Builder() {}
        }
    }

    enum Event {
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

        public static Event create(int value) {
            switch (value) {
                case 1:
                    return GUILD_UPDATE;
                case 10:
                    return CHANNEL_CREATE;
                case 11:
                    return CHANNEL_UPDATE;
                case 12:
                    return CHANNEL_DELETE;
                case 13:
                    return CHANNEL_OVERWRITE_CREATE;
                case 14:
                    return CHANNEL_OVERWRITE_UPDATE;
                case 15:
                    return CHANNEL_OVERWRITE_DELETE;
                case 20:
                    return MEMBER_KICK;
                case 21:
                    return MEMBER_PRUNE;
                case 22:
                    return MEMBER_BAN_ADD;
                case 23:
                    return MEMBER_BAN_REMOVE;
                case 24:
                    return MEMBER_UPDATE;
                case 25:
                    return MEMBER_ROLE_UPDATE;
                case 26:
                    return MEMBER_MOVE;
                case 27:
                    return MEMBER_DISCONNECT;
                case 28:
                    return BOT_ADD;
                case 30:
                    return ROLE_CREATE;
                case 31:
                    return ROLE_UPDATE;
                case 32:
                    return ROLE_DELETE;
                case 40:
                    return INVITE_CREATE;
                case 41:
                    return INVITE_UPDATE;
                case 42:
                    return INVITE_DELETE;
                case 50:
                    return WEBHOOK_CREATE;
                case 51:
                    return WEBHOOK_UPDATE;
                case 52:
                    return WEBHOOK_DELETE;
                case 60:
                    return EMOJI_CREATE;
                case 61:
                    return EMOJI_UPDATE;
                case 62:
                    return EMOJI_DELETE;
                case 72:
                    return MESSAGE_DELETE;
                case 73:
                    return MESSAGE_BULK_DELETE;
                case 74:
                    return MESSAGE_PIN;
                case 75:
                    return MESSAGE_UNPIN;
                case 80:
                    return INTEGRATION_CREATE;
                case 81:
                    return INTEGRATION_UPDATE;
                case 82:
                    return INTEGRATION_DELETE;
                case 83:
                    return STAGE_INSTANCE_CREATE;
                case 84:
                    return STAGE_INSTANCE_UPDATE;
                case 85:
                    return STAGE_INSTANCE_DELETE;
                case 90:
                    return STICKER_CREATE;
                case 91:
                    return STICKER_UPDATE;
                case 92:
                    return STICKER_DELETE;
                case 100:
                    return GUILD_SCHEDULED_EVENT_CREATE;
                case 101:
                    return GUILD_SCHEDULED_EVENT_UPDATE;
                case 102:
                    return GUILD_SCHEDULED_EVENT_DELETE;
                case 110:
                    return THREAD_CREATE;
                case 111:
                    return THREAD_UPDATE;
                case 112:
                    return THREAD_DELETE;
                case 121:
                    return APPLICATION_COMMAND_PERMISSION_UPDATE;
                case 140:
                    return AUTO_MODERATION_RULE_CREATE;
                case 141:
                    return AUTO_MODERATION_RULE_UPDATE;
                case 142:
                    return AUTO_MODERATION_RULE_DELETE;
                case 143:
                    return AUTO_MODERATION_BLOCK_MESSAGE;
                case 144:
                    return AUTO_MODERATION_FLAG_TO_CHANNEL;
                case 145:
                    return AUTO_MODERATION_USER_COMMUNICATION_DISABLED;
                default:
                    throw new IllegalArgumentException();
            }
        }

        Event(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // TODO
    @ImmutableJson
    @JsonDeserialize(as = ImmutableAuditLog.Change.class)
    interface Change {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("new_value")
        Optional<Object> newValue();

        @JsonProperty("old_value")
        Optional<Object> oldValue();

        String key();

        class Builder extends ImmutableAuditLog.Change.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableAuditLog.Builder {
        protected Builder() {}
    }
}
