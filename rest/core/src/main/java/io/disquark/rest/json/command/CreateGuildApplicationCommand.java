package io.disquark.rest.json.command;

import static io.disquark.rest.util.Variables.variables;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableUni;
import io.disquark.rest.json.Locale;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.request.AbstractRequestUni;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.vertx.core.http.HttpMethod;

@ImmutableUni
abstract class CreateGuildApplicationCommand extends AbstractRequestUni<ApplicationCommand> {

    @JsonIgnore
    public abstract Snowflake applicationId();

    @JsonIgnore
    public abstract Snowflake guildId();

    public abstract String name();

    @JsonProperty("name_localizations")
    public abstract Optional<Map<Locale, String>> nameLocalizations();

    public abstract Optional<String> description();

    @JsonProperty("description_localizations")
    public abstract Optional<Map<Locale, String>> descriptionLocalizations();

    public abstract Optional<List<ApplicationCommand.Option>> options();

    @JsonProperty("default_member_permissions")
    public abstract Optional<EnumSet<PermissionFlag>> defaultMemberPermissions();

    @JsonProperty("default_permission")
    public abstract Optional<Boolean> defaultPermission();

    public abstract Optional<ApplicationCommand.Type> type();

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
                .endpoint(Endpoint.create(HttpMethod.POST, "/applications/{application.id}/guilds/{guild.id}/commands"))
                .variables(variables("application.id", applicationId().getValue(), "guild.id", guildId().getValue()))
                .body(this)
                .build();
    }
}
