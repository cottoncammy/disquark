package org.example.rest.resources.application.command;

import static org.example.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;

import org.example.immutables.ImmutableJson;
import org.example.nullableoptional.NullableOptional;
import org.example.nullableoptional.jackson.NullableOptionalFilter;
import org.example.rest.request.Endpoint;
import org.example.rest.request.Request;
import org.example.rest.request.Requestable;
import org.example.rest.resources.Locale;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.permissions.PermissionFlag;

@ImmutableJson
public interface EditGlobalApplicationCommand extends Requestable {

    static Builder builder() {
        return new Builder();
    }

    @JsonIgnore
    Snowflake applicationId();

    @JsonIgnore
    Snowflake commandId();

    Optional<String> name();

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

    @JsonProperty("dm_permission")
    Optional<Boolean> dmPermission();

    @JsonProperty("default_permission")
    Optional<Boolean> defaultPermission();

    Optional<Boolean> nsfw();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/applications/{application.id}/commands/{command.id}"))
                .variables(variables("application.id", applicationId().getValue(), "command.id", commandId().getValue()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableEditGlobalApplicationCommand.Builder {
        protected Builder() {}
    }
}
