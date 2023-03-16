package io.disquark.rest.resources.application.command;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Locale;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.permissions.PermissionFlag;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class EditGuildApplicationCommand extends AbstractRequestUni<ApplicationCommand> {

    @JsonIgnore
    public abstract Snowflake applicationId();

    @JsonIgnore
    public abstract Snowflake guildId();

    @JsonIgnore
    public abstract Snowflake commandId();

    public abstract Optional<String> name();

    @JsonProperty("name_localizations")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Map<Locale, String>> nameLocalizations();

    public abstract Optional<String> description();

    @JsonProperty("description_localizations")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<Map<Locale, String>> descriptionLocalizations();

    public abstract Optional<List<ApplicationCommand.Option>> options();

    @JsonProperty("default_member_permissions")
    @JsonInclude(value = Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    public abstract NullableOptional<EnumSet<PermissionFlag>> defaultMemberPermissions();

    @JsonProperty("default_permission")
    public abstract Optional<Boolean> defaultPermission();

    public abstract Optional<Boolean> nsfw();

    @Override
    public void subscribe(UniSubscriber<? super ApplicationCommand> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(ApplicationCommand.class))
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.PATCH,
                        "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue(),
                        "command.id", commandId().getValue()))
                .body(this)
                .build();
    }
}
