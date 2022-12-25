package org.example.rest.resources.auditlog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Snowflake;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.automod.AutoModerationRule;
import org.example.rest.resources.channel.Channel;

import java.util.Optional;

@ImmutableJson
@JsonDeserialize(as = ImmutableOptionalAuditEntryInfo.class)
public interface OptionalAuditEntryInfo {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("application_id")
    Optional<Snowflake> applicationId();

    @JsonProperty("auto_moderation_rule_name")
    Optional<String> autoModerationRuleName();

    @JsonProperty("auto_moderation_rule_trigger_type")
    Optional<AutoModerationRule.TriggerType> autoModerationRuleTriggerType();

    @JsonProperty("channel_id")
    Optional<Snowflake> channelId();

    Optional<String> count();

    @JsonProperty("delete_member_days")
    Optional<String> deleteMemberDays();

    Optional<Snowflake> id();

    @JsonProperty("members_removed")
    Optional<String> membersRemoved();

    @JsonProperty("message_id")
    Optional<Snowflake> messageId();

    @JsonProperty("role_name")
    Optional<String> roleName();

    Optional<Channel.Overwrite.Type> type();

    class Builder extends ImmutableOptionalAuditEntryInfo.Builder {
        protected Builder() {}
    }
}
