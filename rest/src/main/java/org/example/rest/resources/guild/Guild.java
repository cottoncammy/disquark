package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.jackson.ScopesDeserializer;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.emoji.Emoji;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.sticker.Sticker;
import org.example.rest.resources.user.User;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.oauth2.Scope;
import org.example.rest.resources.permissions.PermissionFlag;
import org.example.rest.resources.permissions.Role;
import org.example.rest.resources.FlagEnum;
import org.immutables.value.Value.Enclosing;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableGuild.class)
public interface Guild {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    String name();

    Optional<String> icon();

    @JsonProperty("icon_hash")
    Optional<String> iconHash();

    Optional<String> splash();

    @JsonProperty("discovery_splash")
    Optional<String> discoverySplash();

    Optional<Boolean> owner();

    @JsonProperty("owner_id")
    Snowflake ownerId();

    Optional<EnumSet<PermissionFlag>> permissions();

    @Deprecated
    Optional<String> region();

    @JsonProperty("afk_channel_id")
    Optional<Snowflake> afkChannelId();

    @JsonProperty("afk_timeout")
    int afkTimeout();

    @JsonProperty("widget_enabled")
    Optional<Boolean> widgetEnabled();

    @JsonProperty("widget_channel_id")
    Optional<Snowflake> widgetChannelId();

    @JsonProperty("verification_level")
    VerificationLevel verificationLevel();

    @JsonProperty("default_message_notifications")
    DefaultMessageNotificationLevel defaultMessageNotifications();

    @JsonProperty("explicit_content_filter")
    ExplicitContentFilterLevel explicitContentFilter();

    List<Role> roles();

    List<Emoji> emojis();

    List<Feature> features();

    @JsonProperty("mfa_level")
    MfaLevel mfaLevel();

    @JsonProperty("application_id")
    Optional<Snowflake> applicationId();

    @JsonProperty("system_channel_id")
    Optional<Snowflake> systemChannelId();

    @JsonProperty("system_channel_flags")
    EnumSet<SystemChannelFlag> systemChannelFlag();

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
    PremiumTier premiumTier();

    @JsonProperty("premium_subscription_count")
    OptionalInt premiumSubscriptionCount();

    @JsonProperty("preferred_locale")
    Locale preferredLocale();

    @JsonProperty("public_updates_channel_id")
    Optional<Snowflake> publicUpdatesChannelId();

    @JsonProperty("max_video_channel_users")
    OptionalInt maxVideoChannelUsers();

    @JsonProperty("approximate_member_count")
    OptionalInt approximateMemberCount();

    @JsonProperty("approximate_presence_count")
    OptionalInt approximatePresenceCount();

    @JsonProperty("welcome_screen")
    Optional<WelcomeScreen> welcomeScreen();

    @JsonProperty("nsfw_level")
    NsfwLevel nsfwLevel();

    Optional<List<Sticker>> stickers();

    @JsonProperty("premium_progress_bar_enabled")
    boolean premiumProgressBarEnabled();

    enum DefaultMessageNotificationLevel {
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
        SUPPRESS_JOIN_NOTIFICATION_REPLIES(3);

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
    @JsonDeserialize(as = ImmutableGuild.Preview.class)
    interface Preview {

        static Builder builder() {
            return new Builder();
        }

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

        class Builder extends ImmutableGuild.Preview.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuild.WidgetSettings.class)
    interface WidgetSettings {

        static Builder builder() {
            return new Builder();
        }

        boolean enabled();

        @JsonProperty("channel_id")
        Optional<Snowflake> channelId();

        class Builder extends ImmutableGuild.WidgetSettings.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuild.Widget.class)
    interface Widget {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        String name();

        @JsonProperty("instant_invite")
        Optional<String> instantInvite();

        List<Channel> channels();

        List<User> members();

        @JsonProperty("presence_count")
        int presenceCount();

        class Builder extends ImmutableGuild.Widget.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuild.Member.class)
    interface Member {

        static Builder builder() {
            return new Builder();
        }

        Optional<User> user();

        Optional<String> nick();

        Optional<String> avatar();

        List<Snowflake> roles();

        @JsonProperty("joined_at")
        Instant joinedAt();

        @JsonProperty("premium_since")
        Optional<Instant> premiumSince();

        boolean deaf();

        boolean mute();

        Optional<Boolean> pending();

        Optional<EnumSet<PermissionFlag>> permissions();

        @JsonProperty("communication_disabled_until")
        Optional<Instant> communicationDisabledUntil();

        class Builder extends ImmutableGuild.Member.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuild.Integration.class)
    interface Integration {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        String name();

        String type();

        Optional<Boolean> enabled();

        Optional<Boolean> syncing();

        @JsonProperty("role_id")
        Optional<Snowflake> roleId();

        @JsonProperty("enable_emoticons")
        Optional<Boolean> enableEmoticons();

        @JsonProperty("expire_behavior")
        Optional<ExpireBehavior> expireBehavior();

        @JsonProperty("expire_grace_period")
        OptionalInt expireGracePeriod();

        Optional<User> user();

        Account account();

        @JsonProperty("synced_at")
        Optional<Instant> syncedAt();

        @JsonProperty("subscriber_count")
        OptionalInt subscriberCount();

        Optional<Boolean> revoked();

        Optional<Application> application();

        Optional<List<Scope>> scopes();

        enum ExpireBehavior {
            REMOVE_ROLE(0),
            KICK(1);

            private final int value;

            ExpireBehavior(int value) {
                this.value = value;
            }

            @JsonValue
            public int getValue() {
                return value;
            }
        }

        @ImmutableJson
        @JsonDeserialize(as = ImmutableGuild.Account.class)
        interface Account {

            static Builder builder() {
                return new Builder();
            }

            String id();

            String name();

            class Builder extends ImmutableGuild.Account.Builder {
                protected Builder() {}
            }
        }

        @ImmutableJson
        @JsonDeserialize(as = ImmutableGuild.Application.class)
        interface Application {

            static Builder builder() {
                return new Builder();
            }

            Snowflake id();

            String name();

            Optional<String> icon();

            String description();

            Optional<User> bot();

            class Builder extends ImmutableGuild.Application.Builder {
                protected Builder() {}
            }
        }

        class Builder extends ImmutableGuild.Integration.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuild.Ban.class)
    interface Ban {

        static Builder builder() {
            return new Builder();
        }

        Optional<String> reason();

        User user();

        class Builder extends ImmutableGuild.Ban.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableGuild.WelcomeScreen.class)
    interface WelcomeScreen {

        static Builder builder() {
            return new Builder();
        }

        Optional<String> description();

        @JsonProperty("welcome_channels")
        List<Channel> welcomeChannels();

        @ImmutableJson
        interface Channel {

            static Builder builder() {
                return new Builder();
            }

            @JsonProperty("channel_id")
            Snowflake channelId();

            String description();

            @JsonProperty("emoji_id")
            Optional<Snowflake> emojiId();

            @JsonProperty("emoji_name")
            Optional<String> emojiName();

            class Builder extends ImmutableGuild.Channel.Builder {
                protected Builder() {}
            }
        }

        class Builder extends ImmutableGuild.WelcomeScreen.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableGuild.Builder {
        protected Builder() {}
    }
}
