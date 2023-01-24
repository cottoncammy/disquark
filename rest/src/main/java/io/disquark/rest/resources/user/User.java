package io.disquark.rest.resources.user;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.FlagEnum;
import io.disquark.rest.resources.Locale;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.guild.Guild;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableUser.class)
public interface User {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    String username();

    String discriminator();

    Optional<String> avatar();

    Optional<Boolean> bot();

    Optional<Boolean> system();

    @JsonProperty("mfa_enabled")
    Optional<Boolean> mfaEnabled();

    Optional<String> banner();

    @JsonProperty("accent_color")
    OptionalInt accentColor();

    Optional<Locale> locale();

    Optional<Boolean> verified();

    Optional<String> email();

    Optional<EnumSet<Flag>> flags();

    @JsonProperty("premium_type")
    Optional<PremiumType> premiumType();

    @JsonProperty("public_flags")
    Optional<EnumSet<Flag>> publicFlags();

    enum Flag implements FlagEnum {
        STAFF(0),
        PARTNER(1),
        HYPESQUAD(2),
        BUG_HUNTER_LEVEL_1(3),
        HYPESQUAD_ONLINE_HOUSE_1(6),
        HYPESQUAD_ONLINE_HOUSE_2(7),
        HYPESQUAD_ONLINE_HOUSE_3(8),
        PREMIUM_EARLY_SUPPORTER(9),
        TEAM_PSEUDO_USER(10),
        BUG_HUNTER_LEVEL_2(14),
        VERIFIED_BOT(16),
        VERIFIED_DEVELOPER(17),
        CERTIFIED_MODERATOR(18),
        BOT_HTTP_INTERACTIONS(19),
        ACTIVE_DEVELOPER(22);

        private final int value;

        Flag(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    enum PremiumType {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
        NONE(0),
        NITRO_CLASSIC(1),
        NITRO(2),
        NITRO_BASIC(3);

        private final int value;

        PremiumType(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableUser.Connection.class)
    interface Connection {

        static Builder builder() {
            return new Builder();
        }

        String id();

        String name();

        Service type();

        Optional<Boolean> revoked();

        Optional<List<Guild.Integration>> integrations();

        boolean verified();

        @JsonProperty("friend_sync")
        boolean friendSync();

        @JsonProperty("show_activity")
        boolean showActivity();

        @JsonProperty("two_way_link")
        boolean twoWayLink();

        VisibilityType visibility();

        enum Service {
            @JsonEnumDefaultValue
            UNKNOWN("unknown"),
            BATTLE_NET("battlenet"),
            EBAY("ebay"),
            EPIC_GAMES("epicgames"),
            FACEBOOK("facebook"),
            GITHUB("github"),
            LEAGUE_OF_LEGENDS("leagueoflegends"),
            PAYPAL("paypal"),
            PLAYSTATION("playstation"),
            REDDIT("reddit"),
            RIOT_GAMES("riotgames"),
            SPOTIFY("spotify"),
            SKYPE("skype"),
            STEAM("steam"),
            TWITCH("twitch"),
            TWITTER("twitter"),
            XBOX("xbox"),
            YOUTUBE("youtube");

            private final String value;

            Service(String value) {
                this.value = value;
            }

            @JsonValue
            public String getValue() {
                return value;
            }
        }

        enum VisibilityType {
            @JsonEnumDefaultValue
            UNKNOWN(-1),
            NONE(0),
            EVERYONE(1);

            private final int value;

            VisibilityType(int value) {
                this.value = value;
            }

            @JsonValue
            public int getValue() {
                return value;
            }
        }

        class Builder extends ImmutableUser.Connection.Builder {
            protected Builder() {
            }
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableUser.ApplicationRoleConnection.class)
    interface ApplicationRoleConnection {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("platform_name")
        Optional<String> platformName();

        @JsonProperty("platform_username")
        Optional<String> platformUsername();

        Map<String, String> metadata();

        class Builder extends ImmutableUser.ApplicationRoleConnection.Builder {
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableUser.Builder {
        protected Builder() {
        }
    }
}
