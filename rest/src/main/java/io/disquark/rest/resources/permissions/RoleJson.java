package io.disquark.rest.resources.permissions;

import java.util.EnumSet;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Role.class)
interface RoleJson {

    Snowflake id();

    String name();

    int color();

    boolean hoist();

    Optional<String> icon();

    @JsonProperty("unicode_emoji")
    Optional<String> unicodeEmoji();

    int position();

    EnumSet<PermissionFlag> permissions();

    boolean managed();

    boolean mentionable();

    Optional<Role.Tags> tags();

    @ImmutableJson
    @JsonDeserialize(as = Role.Tags.class)
    interface TagsJson {

        @JsonProperty("bot_id")
        Optional<Snowflake> botId();

        @JsonProperty("integration_id")
        Optional<Snowflake> integrationId();

        @JsonProperty("premium_subscriber")
        Optional<Void> premiumSubscriber();

        @JsonProperty("subscription_listing_id")
        Optional<Snowflake> subscriptionListingId();

        @JsonProperty("available_for_purchase")
        Optional<Void> availableForPurchase();

        @JsonProperty("guild_connections")
        Optional<Void> guildConnections();
    }
}
