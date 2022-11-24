package org.example.rest.resources.automod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.immutables.value.Value.Enclosing;

import java.util.List;
import java.util.Map;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableAutoModerationRule.class)
public interface AutoModerationRule {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    @JsonProperty("guild_id")
    Snowflake guildId();

    String name();

    @JsonProperty("creator_id")
    Snowflake creatorId();

    @JsonProperty("event_type")
    EventType eventType();

    @JsonProperty("trigger_type")
    TriggerType triggerType();

    @JsonProperty("trigger_metadata")
    TriggerMetadata triggerMetadata();

    List<AutoModerationAction> actions();

    boolean enabled();

    @JsonProperty("exempt_roles")
    List<Snowflake> exemptRoles();

    @JsonProperty("exempt_channels")
    List<Snowflake> exemptChannels();

    enum TriggerType {
        KEYWORD(1),
        SPAM(3),
        KEYWORD_PRESET(4),
        MENTION_SPAM(5);

        private final int value;

        public static TriggerType create(int value) {
            switch (value) {
                case 1:
                    return KEYWORD;
                case 3:
                    return SPAM;
                case 4:
                    return KEYWORD_PRESET;
                case 5:
                    return MENTION_SPAM;
                default:
                    throw new IllegalArgumentException();
            }
        }

        TriggerType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableAutoModerationRule.TriggerMetadata.class)
    interface TriggerMetadata {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("keyword_filter")
        List<String> keywordFilter();

        @JsonProperty("regex_patterns")
        List<String> regexPatterns();

        List<KeywordPresetType> presets();

        @JsonProperty("allow_list")
        List<String> allowList();

        @JsonProperty("mention_total_limit")
        int mentionTotalLimit();

        class Builder extends ImmutableAutoModerationRule.TriggerMetadata.Builder {
            protected Builder() {}
        }
    }

    enum KeywordPresetType {
        PROFANITY(1),
        SEXUAL_CONTENT(2),
        SLURS(3);

        private final int value;

        public static KeywordPresetType create(int value) {
            switch (value) {
                case 1:
                    return PROFANITY;
                case 2:
                    return SEXUAL_CONTENT;
                case 3:
                    return SLURS;
                default:
                    throw new IllegalArgumentException();
            }
        }

        KeywordPresetType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum EventType {
        MESSAGE_SEND(1);

        private final int value;

        public static EventType create(int value) {
            if (value == 1) {
                return MESSAGE_SEND;
            } else {
                throw new IllegalArgumentException();
            }
        }

        EventType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    class Builder extends ImmutableAutoModerationRule.Builder {
        protected Builder() {}
    }
}
