package io.disquark.rest.resources.application;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.FlagEnum;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.oauth2.Scope;
import io.disquark.rest.resources.permissions.PermissionFlag;
import io.disquark.rest.resources.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableApplication.class)
public interface Application {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    String name();

    Optional<String> icon();

    String description();

    @JsonProperty("rpc_origins")
    Optional<List<String>> rpcOrigins();

    @JsonProperty("bot_public")
    boolean botPublic();

    @JsonProperty("bot_require_code_grant")
    boolean botRequireCodeGrant();

    @JsonProperty("terms_of_service_url")
    Optional<String> termsOfServiceUrl();

    @JsonProperty("privacy_policy_url")
    Optional<String> privacyPolicyUrl();

    Optional<User> owner();

    @JsonProperty("verify_key")
    String verifyKey();

    Optional<Team> team();

    @JsonProperty("guild_id")
    Optional<Snowflake> guildId();

    @JsonProperty("primary_sku_id")
    Optional<Snowflake> primarySkuId();

    Optional<String> slug();

    @JsonProperty("cover_image")
    Optional<String> coverImage();

    Optional<EnumSet<Flag>> flags();

    Optional<List<String>> tags();

    @JsonProperty("install_params")
    Optional<InstallParams> installParams();

    @JsonProperty("custom_install_url")
    Optional<String> customInstallUrl();

    @JsonProperty("role_connections_verification_url")
    Optional<String> roleConnectionsVerificationUrl();

    enum Flag implements FlagEnum {
        GATEWAY_PRESENCE(12),
        GATEWAY_PRESENCE_LIMITED(13),
        GATEWAY_GUILD_MEMBERS(14),
        GATEWAY_GUILD_MEMBERS_LIMITED(15),
        VERIFICATION_PENDING_GUILD_LIMIT(16),
        EMBEDDED(17),
        GATEWAY_MESSAGE_CONTENT(18),
        GATEWAY_MESSAGE_CONTENT_LIMITED(19),
        APPLICATION_COMMAND_BADGE(23);

        private final int value;

        Flag(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableApplication.InstallParams.class)
    interface InstallParams {

        static Builder builder() {
            return new Builder();
        }

        List<Scope> scopes();

        EnumSet<PermissionFlag> permissions();

        class Builder extends ImmutableApplication.InstallParams.Builder {
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableApplication.Builder {
        protected Builder() {
        }
    }
}
