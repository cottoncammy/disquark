package io.disquark.rest.json.user;

import java.util.EnumSet;
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

@ImmutableJson
@JsonDeserialize(as = User.class)
interface UserJson {

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

    Optional<EnumSet<User.Flag>> flags();

    @JsonProperty("premium_type")
    Optional<User.PremiumType> premiumType();

    @JsonProperty("public_flags")
    Optional<EnumSet<User.Flag>> publicFlags();

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
}
