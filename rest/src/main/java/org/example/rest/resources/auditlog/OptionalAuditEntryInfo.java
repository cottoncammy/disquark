package org.example.rest.resources.auditlog;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;

@ImmutableJson
public interface OptionalAuditEntryInfo {

    static Builder builder() {
        return new Builder();
    }

    @JsonProperty("application_id")
    Snowflake applicationId();

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
