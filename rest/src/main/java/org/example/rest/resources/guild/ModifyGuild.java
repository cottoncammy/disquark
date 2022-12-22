package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Auditable;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
public interface ModifyGuild extends Auditable, Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake guildId();

    Optional<String> name();

    Optional<String> region();

    @JsonProperty("verification_level")
    Optional<Guild.VerificationLevel> verificationLevel();

    @JsonProperty("default_message_notifications")
    Optional<Guild.DefaultMessageNotificationLevel> defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    Optional<Guild.ExplicitContentFilterLevel> explicitContentFilter();

    @JsonProperty("afk_channel_id")
    Optional<Snowflake> afkChannelId();

    @JsonProperty("afk_timeout")
    OptionalInt afkTimeout();

    Optional<String> icon();

    @JsonProperty("owner_id")
    Optional<Snowflake> ownerId();

    Optional<String> splash();

    @JsonProperty("discovery_splash")
    Optional<String> discoverySplash();

    Optional<String> banner();

    @JsonProperty("system_channel_id")
    Optional<Snowflake> systemChannelId();

    @JsonProperty("system_channel_flags")
    Optional<EnumSet<Guild.SystemChannelFlag>> systemChannelFlags();

    @JsonProperty("rules_channel_id")
    Optional<Snowflake> rulesChannelId();

    @JsonProperty("public_updates_channel_id")
    Optional<Snowflake> publicUpdatesChannelId();

    @JsonProperty("preferred_locale")
    Optional<Locale> preferredLocale();

    Optional<List<Guild.Feature>> features();

    Optional<String> description();

    @JsonProperty("premium_progress_bar_enabled")
    Optional<Boolean> premiumProgressBarEnabled();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}"))
                .variables(Variables.variables().set("guild.id", guildId().getValueAsString()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }

    class Builder extends ImmutableModifyGuild.Builder {
        protected Builder() {}
    }
}
