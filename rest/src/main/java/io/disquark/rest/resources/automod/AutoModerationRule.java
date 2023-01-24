package io.disquark.rest.resources.automod;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

import org.immutables.value.Value.Enclosing;

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
    @JsonDeserialize(as = ImmutableAutoModerationRule.TriggerMetadata.class)
    interface TriggerMetadata {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("keyword_filter")
        Optional<List<String>> keywordFilter();

        @JsonProperty("regex_patterns")
        Optional<List<String>> regexPatterns();

        Optional<List<KeywordPresetType>> presets();

        @JsonProperty("allow_list")
        Optional<List<String>> allowList();

        @JsonProperty("mention_total_limit")
        OptionalInt mentionTotalLimit();

        class Builder extends ImmutableAutoModerationRule.TriggerMetadata.Builder {
            protected Builder() {
            }
        }
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

    class Builder extends ImmutableAutoModerationRule.Builder {
        protected Builder() {
        }
    }
}
