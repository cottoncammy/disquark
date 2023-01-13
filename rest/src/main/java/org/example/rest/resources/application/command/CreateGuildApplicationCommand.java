package org.example.rest.resources.application.command;

import org.example.rest.resources.Locale;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.http.HttpMethod;
import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.permissions.PermissionFlag;
import org.example.nullableoptional.NullableOptional;

import java.util.*;

import static org.example.rest.util.Variables.variables;

@ImmutableJson
public interface CreateGuildApplicationCommand extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonIgnore
    Snowflake guildId();

    String name();

    @JsonProperty("name_localizations")
    Optional<Map<Locale, String>> nameLocalizations();

    Optional<String> description();

    @JsonProperty("description_localizations")
    Optional<Map<Locale, String>> descriptionLocalizations();

    Optional<List<ApplicationCommand.Option>> options();

    @JsonProperty("default_member_permissions")
    Optional<EnumSet<PermissionFlag>> defaultMemberPermissions();

    @JsonProperty("default_permission")
    Optional<Boolean> defaultPermission();

    Optional<ApplicationCommand.Type> type();

    Optional<Boolean> nsfw();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/applications/{application.id}/guilds/{guild.id}/commands"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableCreateGuildApplicationCommand.Builder {
        protected Builder() {}
    }
}
