package io.disquark.rest.json.user;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.guild.Integration;

@ImmutableJson
@JsonDeserialize(as = Connection.class)
interface ConnectionJson {

    String id();

    String name();

    Connection.Service type();

    Optional<Boolean> revoked();

    Optional<List<Integration>> integrations();

    boolean verified();

    @JsonProperty("friend_sync")
    boolean friendSync();

    @JsonProperty("show_activity")
    boolean showActivity();

    @JsonProperty("two_way_link")
    boolean twoWayLink();

    Connection.VisibilityType visibility();

    enum Service {
        @JsonEnumDefaultValue
        UNKNOWN("unknown"),
        BATTLE_NET("battlenet"),
        EBAY("ebay"),
        EPIC_GAMES("epicgames"),
        FACEBOOK("facebook"),
        GITHUB("github"),
        INSTAGRAM("instagram"),
        LEAGUE_OF_LEGENDS("leagueoflegends"),
        PAYPAL("paypal"),
        PLAYSTATION("playstation"),
        REDDIT("reddit"),
        RIOT_GAMES("riotgames"),
        SPOTIFY("spotify"),
        SKYPE("skype"),
        STEAM("steam"),
        TIKTOK("tiktok"),
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
}
