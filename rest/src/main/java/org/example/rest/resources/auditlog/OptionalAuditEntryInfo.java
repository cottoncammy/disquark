package org.example.rest.resources.auditlog;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;

@ImmutableJson
@JsonDeserialize(as = ImmutableOptionalAuditEntryInfo.class)
public interface OptionalAuditEntryInfo {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("application_id")
    Snowflake applicationId();

    @JsonProperty("auto_moderation_rule_name")
    String autoModerationRuleName();

    @JsonProperty("auto_moderation_rule_trigger_type")
    String autoModerationRuleTriggerType();

    @JsonProperty("channel_id")
    Snowflake channelId();

    String count();

    @JsonProperty("delete_member_days")
    String deleteMemberDays();

    Snowflake id();

    @JsonProperty("members_removed")
    String membersRemoved();

    @JsonProperty("message_id")
    Snowflake messageId();

    @JsonProperty("role_name")
    String roleName();

    String type();

    class Builder extends ImmutableOptionalAuditEntryInfo.Builder {
        protected Builder() {}
    }
}
