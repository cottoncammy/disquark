package org.example.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.guild.Guild;
import org.immutables.value.Value.Enclosing;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Enclosing
@ImmutableJson
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
        BOT_HTTP_INTERACTIONS(19);

        private final int value;

        Flag(int value) {
            this.value = value;
        }
    }

    enum PremiumType {
        NONE(0),
        NITRO_CLASSIC(1),
        NITRO(2);

        private final int value;

        PremiumType(int value) {
            this.value = value;
        }
    }

    @ImmutableJson
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

        VisibilityType visibility();

        enum Service {
            BATTLE_NET("battlenet"),
            EPIC_GAMES("epicgames"),
            FACEBOOK("facebook"),
            GITHUB("github"),
            LEAGUE_OF_LEGENDS("leagueoflegends"),
            PLAYSTATION("playstation"),
            REDDIT("reddit"),
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
        }

        enum VisibilityType {
            NONE(0),
            EVERYONE(1);

            private final int value;

            VisibilityType(int value) {
                this.value = value;
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
