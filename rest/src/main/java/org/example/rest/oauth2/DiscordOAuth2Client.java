package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.request.*;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.EditApplicationCommandPermissions;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.user.UpdateUserApplicationRoleConnections;
import org.example.rest.resources.user.User;
import org.example.rest.resources.application.command.GuildApplicationCommandPermissions;
import org.example.rest.resources.oauth2.Authorization;
import org.example.rest.response.Response;
import org.example.rest.util.Hex;

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

    private DiscordOAuth2Client(Vertx vertx, Requester<T> requester, DiscordInteractionsClient.Options interactionsClientOptions) {
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

    public Uni<GuildApplicationCommandPermissions> editApplicationCommandPermissions(EditApplicationCommandPermissions editApplicationCommandPermissions) {
        return requester.request(requireNonNull(editApplicationCommandPermissions, "editApplicationCommandPermissions").asRequest())
                .flatMap(res -> res.as(GuildApplicationCommandPermissions.class));
    }

    public Uni<Guild.Member> getCurrentUserGuildMember(Snowflake guildId) {
        return requester.request(new EmptyRequest("/users/@me/guilds/{guild.id}/member", variables("guild.id", guildId.getValue())))
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Multi<User.Connection> getUserConnections() {
        return requester.request(new EmptyRequest("/users/@me/connections"))
                .flatMap(res -> res.as(User.Connection[].class))
                .onItem().disjoint();
    }

    public Uni<User.ApplicationRoleConnection> getUserApplicationRoleConnection(Snowflake applicationId) {
        return requester.request(new EmptyRequest("/users/@me/applications/{application.id}/role-connection", variables("application.id", applicationId.getValue())))
                .flatMap(res -> res.as(User.ApplicationRoleConnection.class));
    }

    public Uni<User.ApplicationRoleConnection> updateUserApplicationRoleConnection(UpdateUserApplicationRoleConnections updateUserApplicationRoleConnections) {
        return requester.request(requireNonNull(updateUserApplicationRoleConnections, "updateUserApplicationRoleConnections").asRequest())
                .flatMap(res -> res.as(User.ApplicationRoleConnection.class));
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
