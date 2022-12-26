package org.example.rest.oauth2;

import static java.util.Objects.requireNonNull;
import static org.example.rest.util.Variables.variables;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.example.rest.AuthenticatedDiscordClient;
import org.example.rest.DiscordClient;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.request.*;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.EditApplicationCommandPermissions;
import org.example.rest.resources.user.UpdateUserApplicationRoleConnections;
import org.example.rest.resources.user.User;
import org.example.rest.resources.application.command.GuildApplicationCommandPermissions;
import org.example.rest.resources.oauth2.Authorization;
import org.example.rest.response.Response;
import org.example.rest.webhook.DiscordWebhookClient;

import java.util.List;

public class DiscordOAuth2Client<T extends Response> extends AuthenticatedDiscordClient<T> {

    public static <T extends Response> Builder<T> builder(Vertx vertx, AccessTokenSource tokenSource) {
        return new Builder<>(requireNonNull(vertx), requireNonNull(tokenSource));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Response> DiscordOAuth2Client<T> create(Vertx vertx, AccessTokenSource tokenSource) {
        return (DiscordOAuth2Client<T>) builder(vertx, tokenSource).build();
    }

    public static <T extends Response> DiscordOAuth2Client<T> create(BearerTokenSource tokenSource) {
        return create(requireNonNull(tokenSource).getVertx(), tokenSource);
    }

    private DiscordOAuth2Client(Vertx vertx, Requester<T> requester) {
        // TODO reuse requester between clients, but new ratelimiter (shared) between interactions + webhook clients. this will be complex (bucketratelimitrequester can't be reused)
        super(vertx, requester, DiscordInteractionsClient.create(vertx), DiscordWebhookClient.create(vertx));
    }

    public Uni<GuildApplicationCommandPermissions> editApplicationCommandPermissions(EditApplicationCommandPermissions editApplicationCommandPermissions) {
        return requester.request(editApplicationCommandPermissions.asRequest())
                .flatMap(res -> res.as(GuildApplicationCommandPermissions.class));
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
        return requester.request(updateUserApplicationRoleConnections.asRequest())
                .flatMap(res -> res.as(User.ApplicationRoleConnection.class));
    }

    public Uni<Authorization> getCurrentAuthorizationInformation() {
        return requester.request(new EmptyRequest("/oauth2/@me")).flatMap(res -> res.as(Authorization.class));
    }

    public static class Builder<T extends Response> extends DiscordClient.Builder<T, DiscordOAuth2Client<T>> {

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            super(vertx, tokenSource);
        }

        @Override
        @SuppressWarnings("unchecked")
        public DiscordOAuth2Client<T> build() {
            if (requesterFactory == null) {
                requesterFactory = (RequesterFactory<T>) RequesterFactory.DEFAULT_HTTP_REQUESTER;
            }
            return new DiscordOAuth2Client<>(vertx, requesterFactory.apply(this));
        }
    }
}
