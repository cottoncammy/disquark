package io.disquark.rest.resources.application.command;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import io.disquark.immutables.ImmutableJson;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.request.Requestable;
import io.disquark.rest.resources.Locale;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.permissions.PermissionFlag;
import io.vertx.core.http.HttpMethod;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
public interface BulkOverwriteGuildApplicationCommands extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @Nullable
    @JsonIgnore
    Snowflake applicationId();

    @Nullable
    @JsonIgnore
    Snowflake guildId();

    @JsonValue
    List<GuildApplicationCommandOverwrite> guildApplicationCommandOverwrites();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/guilds/{guild.id}/commands"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue()))
                .body(this)
                .build();
    }

    @ImmutableJson
    interface GuildApplicationCommandOverwrite {

        static Builder builder() {
            return new Builder();
        }

        Optional<Snowflake> id();

        String name();

        @JsonProperty("name_localizations")
        @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
        NullableOptional<Map<Locale, String>> nameLocalizations();

        Optional<String> description();

        @JsonProperty("description_localizations")
        @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
        NullableOptional<Map<Locale, String>> descriptionLocalizations();

        Optional<List<ApplicationCommand.Option>> options();

        @JsonProperty("default_member_permissions")
        @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
        NullableOptional<EnumSet<PermissionFlag>> defaultMemberPermissions();

        @JsonProperty("default_permission")
        Optional<Boolean> defaultPermission();

        Optional<ApplicationCommand.Type> type();

        Optional<Boolean> nsfw();

        class Builder extends ImmutableBulkOverwriteGuildApplicationCommands.GuildApplicationCommandOverwrite.Builder {
            protected Builder() {
            }
        }
    }

    class Builder extends ImmutableBulkOverwriteGuildApplicationCommands.Builder {
        protected Builder() {
        }
    }
}
