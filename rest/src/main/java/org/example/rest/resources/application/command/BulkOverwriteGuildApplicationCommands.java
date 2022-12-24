package org.example.rest.resources.application.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.permissions.PermissionFlag;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ImmutableJson
public interface BulkOverwriteGuildApplicationCommands extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonIgnore
    Snowflake guildId();

    @JsonValue
    List<GuildApplicationCommandOverwrite> guildApplicationCommandOverwrites();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PUT, "/applications/{application.id}/guilds/{guild.id}/commands"))
                .variables(Variables.variables().set("application.id", applicationId().getValueAsString()).set("guild.id", guildId().getValueAsString()))
                .body(this)
                .build();
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    interface GuildApplicationCommandOverwrite {

        Optional<Snowflake> id();

        String name();

        @JsonProperty("name_localizations")
        Optional<Map<Locale, String>> nameLocalizations();

        String description();

        @JsonProperty("description_localizations")
        Optional<Map<Locale, String>> descriptionLocalizations();

        Optional<List<ApplicationCommand.Option>> options();

        @JsonProperty("default_member_permissions")
        Optional<EnumSet<PermissionFlag>> defaultMemberPermissions();

        @JsonProperty("default_permission")
        Optional<Boolean> defaultPermission();

        Optional<ApplicationCommand.Type> type();

        Optional<Boolean> nsfw();
    }

    class Builder extends ImmutableBulkOverwriteGuildApplicationCommands.Builder {
        protected Builder() {}
    }
}
