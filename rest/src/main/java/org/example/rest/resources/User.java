package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.guild.Guild;
import org.immutables.value.Value.Enclosing;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static java.util.Objects.requireNonNull;

@Enclosing
@ImmutableJson
@JsonInclude(Include.NON_ABSENT)
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

    enum Flag {
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

        public static Flag create(int value) {
            switch (value) {
                case 0:
                    return STAFF;
                case 1:
                    return PARTNER;
                case 2:
                    return HYPESQUAD;
                case 3:
                    return BUG_HUNTER_LEVEL_1;
                case 6:
                    return HYPESQUAD_ONLINE_HOUSE_1;
                case 7:
                    return HYPESQUAD_ONLINE_HOUSE_2;
                case 8:
                    return HYPESQUAD_ONLINE_HOUSE_3;
                case 9:
                    return PREMIUM_EARLY_SUPPORTER;
                case 10:
                    return TEAM_PSEUDO_USER;
                case 14:
                    return BUG_HUNTER_LEVEL_2;
                case 16:
                    return VERIFIED_BOT;
                case 17:
                    return VERIFIED_DEVELOPER;
                case 18:
                    return CERTIFIED_MODERATOR;
                case 19:
                    return BOT_HTTP_INTERACTIONS;
                case 22:
                    return ACTIVE_DEVELOPER;
                default:
                    throw new IllegalArgumentException();
            }
        }

        Flag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    enum PremiumType {
        NONE(0),
        NITRO_CLASSIC(1),
        NITRO(2),
        NITRO_BASIC(3);

        private final int value;

        public static PremiumType create(int value) {
            switch (value) {
                case 0:
                    return NONE;
                case 1:
                    return NITRO_CLASSIC;
                case 2:
                    return NITRO;
                case 3:
                    return NITRO_BASIC;
                default:
                    throw new IllegalArgumentException();
            }
        }

        PremiumType(int value) {
            this.value = value;
        }

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

            public static Service create(String value) {
                switch (requireNonNull(value)) {
                    case "battlenet":
                        return BATTLE_NET;
                    case "ebay":
                        return EBAY;
                    case "epicgames":
                        return EPIC_GAMES;
                    case "facebook":
                        return FACEBOOK;
                    case "github":
                        return GITHUB;
                    case "leagueoflegends":
                        return LEAGUE_OF_LEGENDS;
                    case "paypal":
                        return PAYPAL;
                    case "playstation":
                        return PLAYSTATION;
                    case "reddit":
                        return REDDIT;
                    case "riotgames":
                        return RIOT_GAMES;
                    case "spotify":
                        return SPOTIFY;
                    case "skype":
                        return SKYPE;
                    case "steam":
                        return STEAM;
                    case "twitch":
                        return TWITCH;
                    case "twitter":
                        return TWITTER;
                    case "xbox":
                        return XBOX;
                    case "youtube":
                        return YOUTUBE;
                    default:
                        throw new IllegalArgumentException();
                }
            }

            Service(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }
        }

        enum VisibilityType {
            NONE(0),
            EVERYONE(1);

            private final int value;

            public static VisibilityType create(int value) {
                if (value == 0) {
                    return NONE;
                } else if (value == 1) {
                    return EVERYONE;
                } else {
                    throw new IllegalArgumentException();
                }
            }

            VisibilityType(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        class Builder extends ImmutableUser.Connection.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableUser.Builder {
        protected Builder() {}
    }
}
