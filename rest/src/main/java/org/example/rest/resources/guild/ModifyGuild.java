package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;
import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.jackson.ImageDataSerializer;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;
import org.example.nullableoptional.NullableOptional;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
public interface ModifyGuild extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    NullableOptional<String> name();

    NullableOptional<String> region();

    @JsonProperty("verification_level")
    NullableOptional<Guild.VerificationLevel> verificationLevel();

    @JsonProperty("default_message_notifications")
    NullableOptional<Guild.DefaultMessageNotificationLevel> defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    NullableOptional<Guild.ExplicitContentFilterLevel> explicitContentFilter();

    @JsonProperty("afk_channel_id")
    NullableOptional<Snowflake> afkChannelId();

    @JsonProperty("afk_timeout")
    NullableOptional<Integer> afkTimeout();

    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    NullableOptional<Buffer> icon();

    @JsonProperty("owner_id")
    @JsonInclude(Include.NON_ABSENT)
    Optional<Snowflake> ownerId();

    NullableOptional<String> splash();

    @JsonProperty("discovery_splash")
    NullableOptional<String> discoverySplash();

    NullableOptional<String> banner();

    @JsonProperty("system_channel_id")
    NullableOptional<Snowflake> systemChannelId();

    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("system_channel_flags")
    Optional<EnumSet<Guild.SystemChannelFlag>> systemChannelFlags();

    @JsonProperty("rules_channel_id")
    NullableOptional<Snowflake> rulesChannelId();

    @JsonProperty("public_updates_channel_id")
    NullableOptional<Snowflake> publicUpdatesChannelId();

    @JsonProperty("preferred_locale")
    NullableOptional<Locale> preferredLocale();

    @JsonInclude(Include.NON_ABSENT)
    Optional<List<Guild.Feature>> features();

    NullableOptional<String> description();

    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("premium_progress_bar_enabled")
    Optional<Boolean> premiumProgressBarEnabled();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuild.Builder {
        protected Builder() {}
    }
}
