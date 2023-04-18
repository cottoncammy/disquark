package io.disquark.rest.json.guild;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.FlagEnum;
import io.disquark.rest.json.Locale;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.emoji.Emoji;
import io.disquark.rest.json.role.Role;
import io.disquark.rest.json.sticker.Sticker;
import io.disquark.rest.json.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Guild.class)
interface GuildJson {

    Snowflake id();

    String name();

    Optional<String> icon();

    Optional<String> splash();

    @JsonProperty("discovery_splash")
    Optional<String> discoverySplash();

    @JsonProperty("owner_id")
    Snowflake ownerId();

    @JsonProperty("afk_channel_id")
    Optional<Snowflake> afkChannelId();

    @JsonProperty("afk_timeout")
    int afkTimeout();

    @JsonProperty("widget_enabled")
    Optional<Boolean> widgetEnabled();

    @JsonProperty("widget_channel_id")
    Optional<Snowflake> widgetChannelId();

    @JsonProperty("verification_level")
    Guild.VerificationLevel verificationLevel();

    @JsonProperty("default_message_notifications")
    Guild.DefaultMessageNotificationLevel defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    Guild.ExplicitContentFilterLevel explicitContentFilter();

    List<Role> roles();

    List<Emoji> emojis();

    List<Guild.Feature> features();

    @JsonProperty("mfa_level")
    Guild.MfaLevel mfaLevel();

    @JsonProperty("application_id")
    Optional<Snowflake> applicationId();

    @JsonProperty("system_channel_id")
    Optional<Snowflake> systemChannelId();

    @JsonProperty("system_channel_flags")
    EnumSet<Guild.SystemChannelFlag> systemChannelFlags();

    @JsonProperty("rules_channel_id")
    Optional<Snowflake> rulesChannelId();

    @JsonProperty("max_presences")
    OptionalInt maxPresences();

    @JsonProperty("max_members")
    OptionalInt maxMembers();

    @JsonProperty("vanity_url_code")
    Optional<String> vanityUrlCode();

    Optional<String> description();

    Optional<String> banner();

    @JsonProperty("premium_tier")
    Guild.PremiumTier premiumTier();

    @JsonProperty("premium_subscription_count")
    OptionalInt premiumSubscriptionCount();

    @JsonProperty("preferred_locale")
    Locale preferredLocale();

    @JsonProperty("public_updates_channel_id")
    Optional<Snowflake> publicUpdatesChannelId();

    @JsonProperty("max_video_channel_users")
    OptionalInt maxVideoChannelUsers();

    @JsonProperty("max_stage_video_channel_users")
    OptionalInt maxStageVideoChannelUsers();

    @JsonProperty("approximate_member_count")
    OptionalInt approximateMemberCount();

    @JsonProperty("approximate_presence_count")
    OptionalInt approximatePresenceCount();

    @JsonProperty("welcome_screen")
    Optional<WelcomeScreen> welcomeScreen();

    @JsonProperty("nsfw_level")
    Guild.NsfwLevel nsfwLevel();

    Optional<List<Sticker>> stickers();

    @JsonProperty("premium_progress_bar_enabled")
    boolean premiumProgressBarEnabled();

    enum DefaultMessageNotificationLevel {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        ALL_MESSAGES(0),
        ONLY_MENTIONS(1);

        private final int value;

        DefaultMessageNotificationLevel(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum ExplicitContentFilterLevel {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        DISABLED(0),
        MEMBERS_WITHOUT_ROLES(1),
        ALL_MEMBERS(2);

        private final int value;

        ExplicitContentFilterLevel(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum MfaLevel {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        NONE(0),
        ELEVATED(1);

        private final int value;

        MfaLevel(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum VerificationLevel {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        NONE(0),
        LOW(1),
        MEDIUM(2),
        HIGH(3),
        VERY_HIGH(4);

        private final int value;

        VerificationLevel(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum NsfwLevel {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        DEFAULT(0),
        EXPLICIT(1),
        SAFE(2),
        AGE_RESTRICTED(3);

        private final int value;

        NsfwLevel(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum PremiumTier {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        NONE(0),
        TIER_1(1),
        TIER_2(2),
        TIER_3(3);

        private final int value;

        PremiumTier(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    enum SystemChannelFlag implements FlagEnum {
        SUPPRESS_JOIN_NOTIFICATIONS(0),
        SUPPRESS_PREMIUM_SUBSCRIPTIONS(1),
        SUPPRESS_GUILD_REMINDER_NOTIFICATIONS(2),
        SUPPRESS_JOIN_NOTIFICATION_REPLIES(3),
        SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATIONS(4),
        SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATION_REPLIES(5);

        private final int value;

        SystemChannelFlag(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    enum Feature {
        @JsonEnumDefaultValue
        UNKNOWN,
        ANIMATED_BANNER,
        ANIMATED_ICON,
        APPLICATION_COMMAND_PERMISSIONS_V2,
        AUTO_MODERATION,
        BANNER,
        COMMUNITY,
        CREATOR_MONETIZABLE_PROVISIONAL,
        CREATOR_STORE_PAGE,
        DEVELOPER_SUPPORT_SERVER,
        DISCOVERABLE,
        FEATURABLE,
        INVITES_DISABLED,
        INVITE_SPLASH,
        MEMBER_VERIFICATION_GATE_ENABLED,
        MORE_STICKERS,
        NEWS,
        PARTNERED,
        PREVIEW_ENABLED,
        ROLE_ICONS,
        ROLE_SUBSCRIPTIONS_AVAILABLE_FOR_PURCHASE,
        ROLE_SUBSCRIPTIONS_ENABLED,
        TICKETED_EVENTS_ENABLED,
        VANITY_URL,
        VERIFIED,
        VIP_REGIONS,
        WELCOME_SCREEN_ENABLED
    }

    @ImmutableJson
    @JsonDeserialize(as = Guild.Preview.class)
    interface PreviewJson {

        Snowflake id();

        String name();

        Optional<String> icon();

        Optional<String> splash();

        @JsonProperty("discovery_splash")
        Optional<String> discoverySplash();

        List<Emoji> emojis();

        List<Feature> features();

        @JsonProperty("approximate_member_count")
        int approximateMemberCount();

        @JsonProperty("approximate_presence_count")
        int approximatePresenceCount();

        Optional<String> description();

        List<Sticker> stickers();
    }

    @ImmutableJson
    @JsonDeserialize(as = Guild.WidgetSettings.class)
    interface WidgetSettingsJson {

        boolean enabled();

        @JsonProperty("channel_id")
        Optional<Snowflake> channelId();
    }

    @ImmutableJson
    @JsonDeserialize(as = Guild.Ban.class)
    interface BanJson {

        Optional<String> reason();

        User user();
    }
}
