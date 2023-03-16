package io.disquark.rest.resources.auditlog;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.automod.AutoModerationRule;
import io.disquark.rest.resources.channel.Channel;

@ImmutableJson
@JsonDeserialize(as = AuditEntryInfo.class)
interface AuditEntryInfoJson {

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
}
