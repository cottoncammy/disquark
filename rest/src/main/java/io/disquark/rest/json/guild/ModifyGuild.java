package io.disquark.rest.json.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.jackson.ImageDataSerializer;
import io.disquark.rest.json.Locale;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Auditable;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.buffer.Buffer;

import org.immutables.value.Value.Redacted;

@ImmutableUni
@JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
abstract class ModifyGuild extends AbstractRequestUni<Guild> implements Auditable {

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract NullableOptional<String> name();

    public abstract NullableOptional<String> region();

    @JsonProperty("verification_level")
    public abstract NullableOptional<GuildJson.VerificationLevel> verificationLevel();

    @JsonProperty("default_message_notifications")
    public abstract NullableOptional<GuildJson.DefaultMessageNotificationLevel> defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    public abstract NullableOptional<GuildJson.ExplicitContentFilterLevel> explicitContentFilter();

    @JsonProperty("afk_channel_id")
    public abstract NullableOptional<Snowflake> afkChannelId();

    @JsonProperty("afk_timeout")
    public abstract NullableOptional<Integer> afkTimeout();

    @Redacted
    @JsonSerialize(contentUsing = ImageDataSerializer.class)
    public abstract NullableOptional<Buffer> icon();

    @JsonProperty("owner_id")
    @JsonInclude(Include.NON_ABSENT)
    public abstract Optional<Snowflake> ownerId();

    public abstract NullableOptional<String> splash();

    @JsonProperty("discovery_splash")
    public abstract NullableOptional<String> discoverySplash();

    public abstract NullableOptional<String> banner();

    @JsonProperty("system_channel_id")
    public abstract NullableOptional<Snowflake> systemChannelId();

    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("system_channel_flags")
    public abstract Optional<EnumSet<GuildJson.SystemChannelFlag>> systemChannelFlags();

    @JsonProperty("rules_channel_id")
    public abstract NullableOptional<Snowflake> rulesChannelId();

    @JsonProperty("public_updates_channel_id")
    public abstract NullableOptional<Snowflake> publicUpdatesChannelId();

    @JsonProperty("preferred_locale")
    public abstract NullableOptional<Locale> preferredLocale();

    @JsonInclude(Include.NON_ABSENT)
    public abstract Optional<List<GuildJson.Feature>> features();

    public abstract NullableOptional<String> description();

    @JsonInclude(Include.NON_ABSENT)
    @JsonProperty("premium_progress_bar_enabled")
    public abstract Optional<Boolean> premiumProgressBarEnabled();

    @Override
    public void subscribe(UniSubscriber<? super Guild> downstream) {
        requester().request(asRequest()).flatMap(res -> res.as(Guild.class)).subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/guilds/{guild.id}"))
                .variables(variables("guild.id", guildId().getValue()))
                .body(this)
                .auditLogReason(auditLogReason())
                .build();
    }
}
