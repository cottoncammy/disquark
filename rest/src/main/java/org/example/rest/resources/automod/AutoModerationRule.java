package org.example.rest.resources.automod;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.util.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.immutables.value.Value.Enclosing;

import java.util.List;

@Enclosing
@ImmutableJson
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

        TriggerType(int value) {
            this.value = value;
        }
    }

    @ImmutableJson
    interface TriggerMetadata {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("keyword_filter")
        List<String> keywordFilter();

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

        KeywordPresetType(int value) {
            this.value = value;
        }
    }

    enum EventType {
        MESSAGE_SEND(1);

        private final int value;

        EventType(int value) {
            this.value = value;
        }
    }

    class Builder extends ImmutableAutoModerationRule.Builder {
        protected Builder() {}
    }
}
