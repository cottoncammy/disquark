package org.example.rest.resources.application.command;

import static org.example.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.permissions.PermissionFlag;

@ImmutableJson
public interface CreateGlobalApplicationCommand extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    String name();

    @JsonProperty("name_localizations")
    Optional<Map<Locale, String>> nameLocalizations();

    Optional<String> description();

    @JsonProperty("description_localizations")
    Optional<Map<Locale, String>> descriptionLocalizations();

    Optional<List<ApplicationCommand.Option>> options();

    @JsonProperty("default_member_permissions")
    Optional<EnumSet<PermissionFlag>> defaultMemberPermissions();

    @JsonProperty("dm_permission")
    Optional<Boolean> dmPermission();

    @JsonProperty("default_permission")
    Optional<Boolean> defaultPermission();

    Optional<ApplicationCommand.Type> type();

    Optional<Boolean> nsfw();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.POST, "/applications/{application.id}/commands"))
                .variables(variables("application.id", applicationId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableCreateGlobalApplicationCommand.Builder {
        protected Builder() {
        }
    }
}
