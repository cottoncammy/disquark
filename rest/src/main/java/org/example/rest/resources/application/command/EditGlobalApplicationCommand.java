package org.example.rest.resources.application.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(Include.NON_ABSENT)
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

    Optional<Boolean> nsfw();

    @Override
    default Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH, "/applications/{application.id}/commands/{command.id}"))
                .variables(Variables.variables().set("application.id", applicationId().getValueAsString()).set("command.id", commandId().getValueAsString()))
                .body(this)
                .build();
    }

    class Builder extends ImmutableEditGlobalApplicationCommand.Builder {
        protected Builder() {}
    }
}
