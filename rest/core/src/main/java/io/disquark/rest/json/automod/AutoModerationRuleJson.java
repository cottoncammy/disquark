package io.disquark.rest.json.automod;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = AutoModerationRule.class)
interface AutoModerationRuleJson {

    Snowflake id();

    @JsonProperty("guild_id")
    Snowflake guildId();

    String name();

    @JsonProperty("creator_id")
    Snowflake creatorId();

    @JsonProperty("event_type")
    AutoModerationRule.EventType eventType();

    @JsonProperty("trigger_type")
    AutoModerationRule.TriggerType triggerType();

    @JsonProperty("trigger_metadata")
    AutoModerationRule.TriggerMetadata triggerMetadata();

    List<AutoModerationAction> actions();

    boolean enabled();

    @JsonProperty("exempt_roles")
    List<Snowflake> exemptRoles();

    @JsonProperty("exempt_channels")
    List<Snowflake> exemptChannels();

    enum TriggerType {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        KEYWORD(1),
        SPAM(3),
        KEYWORD_PRESET(4),
        MENTION_SPAM(5);

        private final int value;

        TriggerType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = AutoModerationRule.TriggerMetadata.class)
    interface TriggerMetadataJson {

        @JsonProperty("keyword_filter")
        Optional<List<String>> keywordFilter();

        @JsonProperty("regex_patterns")
        Optional<List<String>> regexPatterns();

        Optional<List<AutoModerationRule.KeywordPresetType>> presets();

        @JsonProperty("allow_list")
        Optional<List<String>> allowList();

        @JsonProperty("mention_total_limit")
        OptionalInt mentionTotalLimit();
    }

    enum KeywordPresetType {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        PROFANITY(1),
        SEXUAL_CONTENT(2),
        SLURS(3);

        private final int value;

        KeywordPresetType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum EventType {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        MESSAGE_SEND(1);

        private final int value;

        EventType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }
}
