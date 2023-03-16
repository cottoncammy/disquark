package io.disquark.rest.oauth2;

import static io.disquark.rest.util.Variables.variables;
import static java.util.Objects.requireNonNull;

import io.disquark.rest.AuthenticatedDiscordClient;
import io.disquark.rest.interactions.DiscordInteractionsClient;
import io.disquark.rest.request.AccessTokenSource;
import io.disquark.rest.request.EmptyRequest;
import io.disquark.rest.request.Requester;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.application.command.EditApplicationCommandPermissionsUni;
import io.disquark.rest.resources.guild.Guild;
import io.disquark.rest.resources.oauth2.Authorization;
import io.disquark.rest.resources.user.UpdateUserApplicationRoleConnectionsUni;
import io.disquark.rest.resources.user.User;
import io.disquark.rest.response.Response;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;

public class DiscordOAuth2Client<T extends Response> extends AuthenticatedDiscordClient<T> {

    public static <T extends Response> Builder<T> builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder<>(requireNonNull(vertx, "vertx"), requireNonNull(tokenSource, "tokenSource"));
    }

    public static <T extends Response> Builder<T> builder(BearerTokenSource tokenSource) {
        return builder(requireNonNull(tokenSource, "tokenSource").getVertx(), tokenSource);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordOAuth2Client<T> create(Vertx vertx, AccessTokenSource tokenSource) {
        return (DiscordOAuth2Client<T>) builder(vertx, tokenSource).build();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordOAuth2Client<T> create(BearerTokenSource tokenSource) {
        return (DiscordOAuth2Client<T>) builder(tokenSource).build();
    }

    private DiscordOAuth2Client(Vertx vertx, Requester<T> requester,
            DiscordInteractionsClient.Options interactionsClientOptions) {
        super(vertx, requester, interactionsClientOptions);
    }

    @Override
    protected DiscordInteractionsClient<T> buildInteractionsClient() {
        String verifyKey = interactionsClientOptions.getVerifyKey();
        if (verifyKey == null) {
            verifyKey = getCurrentAuthorizationInformation().map(auth -> auth.application().verifyKey()).await().indefinitely();
        }

        return buildInteractionsClient(DiscordInteractionsClient.builder(vertx, verifyKey));
    }

    public EditApplicationCommandPermissionsUni editApplicationCommandPermissions(Snowflake applicationId, Snowflake guildId,
            Snowflake commandId) {
        return (EditApplicationCommandPermissionsUni) deferredUni(
                () -> new EditApplicationCommandPermissionsUni(requester, applicationId, guildId, commandId));
    }

    public Uni<Guild.Member> getCurrentUserGuildMember(Snowflake guildId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/users/@me/guilds/{guild.id}/member",
                variables("guild.id", requireNonNull(guildId, "guildId").getValue()))))
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Multi<User.Connection> getUserConnections() {
        return requester.request(new EmptyRequest("/users/@me/connections"))
                .flatMap(res -> res.as(User.Connection[].class))
                .onItem().disjoint();
    }

    public Uni<User.ApplicationRoleConnection> getUserApplicationRoleConnection(Snowflake applicationId) {
        return deferredUni(() -> requester.request(new EmptyRequest("/users/@me/applications/{application.id}/role-connection",
                variables("application.id", requireNonNull(applicationId, "applicationId").getValue()))))
                .flatMap(res -> res.as(User.ApplicationRoleConnection.class));
    }

    public UpdateUserApplicationRoleConnectionsUni updateUserApplicationRoleConnection(Snowflake applicationId) {
        return (UpdateUserApplicationRoleConnectionsUni) deferredUni(
                () -> new UpdateUserApplicationRoleConnectionsUni(requester, applicationId));
    }

    public Uni<Authorization> getCurrentAuthorizationInformation() {
        return requester.request(new EmptyRequest("/oauth2/@me")).flatMap(res -> res.as(Authorization.class));
    }

    public static class Builder<T extends Response> extends AuthenticatedDiscordClient.Builder<T, DiscordOAuth2Client<T>> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        public DiscordOAuth2Client<T> build() {
            return new DiscordOAuth2Client<>(vertx, getRequesterFactory().apply(this), getInteractionsClientOptions());
        }
    }
}
