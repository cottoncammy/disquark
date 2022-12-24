package org.example.rest;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.uritemplate.Variables;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.request.EmptyRequest;
import org.example.rest.request.Requester;
import org.example.rest.request.RequesterFactory;
import org.example.rest.request.ratelimit.GlobalRateLimiter;
import org.example.rest.request.ratelimit.RateLimitStrategy;
import org.example.rest.resources.application.Application;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.application.command.*;
import org.example.rest.resources.guild.ModifyCurrentMember;
import org.example.rest.resources.guild.ModifyCurrentUserVoiceState;
import org.example.rest.resources.user.GetCurrentUserGuilds;
import org.example.rest.resources.user.ModifyCurrentUser;
import org.example.rest.resources.user.User;
import org.example.rest.resources.guild.Guild;
import org.example.rest.response.Response;

import static java.util.Objects.requireNonNull;

public abstract class DiscordClient<T extends Response> {
    protected final Vertx vertx;
    protected final Requester<T> requester;

    protected DiscordClient(Vertx vertx, Requester<T> requester) {
        this.vertx = vertx;
        this.requester = requester;
    }

    public Multi<ApplicationCommand> getGlobalApplicationCommands(Snowflake applicationId, boolean withLocalizations) {
        return requester.request(new EmptyRequest("/applications/{application.id}/commands{?with_localizations}", Variables.variables(JsonObject.of("application.id", applicationId.getValue(), "with_localizations", withLocalizations))))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Uni<ApplicationCommand> createGlobalApplicationCommand(CreateGlobalApplicationCommand createGlobalApplicationCommand) {
        return requester.request(createGlobalApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> getGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/commands/{command.id}", Variables.variables().set("application.id", applicationId.getValueAsString()).set("command.id", commandId.getValueAsString())))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> editGlobalApplicationCommand(EditGlobalApplicationCommand editGlobalApplicationCommand) {
        return requester.request(editGlobalApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<Void> deleteGlobalApplicationCommand(Snowflake applicationId, Snowflake commandId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/applications/{application.id}/commands/{command.id}", Variables.variables().set("application.id", applicationId.getValueAsString()).set("command.id", commandId.getValueAsString())))
                .replaceWithVoid();
    }

    public Multi<ApplicationCommand> bulkOverwriteGlobalApplicationCommands(BulkOverwriteGlobalApplicationCommands bulkOverwriteGlobalApplicationCommands) {
        return requester.request(bulkOverwriteGlobalApplicationCommands.asRequest())
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Multi<ApplicationCommand> getGuildApplicationCommands(Snowflake applicationId, Snowflake guildId, boolean withLocalizations) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands{?with_localizations}", Variables.variables(JsonObject.of("application.id", applicationId.getValue(), "guild.id", guildId.getValue(), "with_localizations", withLocalizations))))
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Uni<ApplicationCommand> createGuildApplicationCommand(CreateGuildApplicationCommand createGuildApplicationCommand) {
        return requester.request(createGuildApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> getGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/{command.id}", Variables.variables().set("application.id", applicationId.getValueAsString()).set("guild.id", guildId.getValueAsString()).set("command.id", commandId.getValueAsString())))
                .flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<ApplicationCommand> editGuildApplicationCommand(EditGuildApplicationCommand editGuildApplicationCommand) {
        return requester.request(editGuildApplicationCommand.asRequest()).flatMap(res -> res.as(ApplicationCommand.class));
    }

    public Uni<Void> deleteGuildApplicationCommand(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest(HttpMethod.DELETE, "/applications/{application.id}/guilds/{guild.id}/commands/{command.id}", Variables.variables().set("application.id", applicationId.getValueAsString()).set("guild.id", guildId.getValueAsString()).set("command.id", commandId.getValueAsString())))
                .replaceWithVoid();
    }

    public Multi<ApplicationCommand> bulkOverwriteGuildApplicationCommands(BulkOverwriteGuildApplicationCommands bulkOverwriteGuildApplicationCommands) {
        return requester.request(bulkOverwriteGuildApplicationCommands.asRequest())
                .flatMap(res -> res.as(ApplicationCommand[].class))
                .onItem().disjoint();
    }

    public Multi<GuildApplicationCommandPermissions> getGuildApplicationCommandPermissions(Snowflake applicationId, Snowflake guildId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/permissions", Variables.variables().set("application.id", applicationId.getValueAsString()).set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions[].class))
                .onItem().disjoint();
    }

    public Uni<GuildApplicationCommandPermissions> getApplicationCommandPermissions(Snowflake applicationId, Snowflake guildId, Snowflake commandId) {
        return requester.request(new EmptyRequest("/applications/{application.id}/guilds/{guild.id}/commands/{command.id}/permissions", Variables.variables().set("application.id", applicationId.getValueAsString()).set("guild.id", guildId.getValueAsString()).set("command.id", commandId.getValueAsString())))
                .flatMap(res -> res.as(GuildApplicationCommandPermissions.class));
    }

    public Uni<Guild.Member> modifyCurrentMember(ModifyCurrentMember modifyCurrentMember) {
        return requester.request(modifyCurrentMember.asRequest()).flatMap(res -> res.as(Guild.Member.class));
    }

    public Uni<Void> modifyCurrentUserVoiceState(ModifyCurrentUserVoiceState modifyCurrentUserVoiceState) {
        return requester.request(modifyCurrentUserVoiceState.asRequest()).replaceWithVoid();
    }

    public Uni<User> getCurrentUser() {
        return requester.request(new EmptyRequest("/users/@me")).flatMap(res -> res.as(User.class));
    }

    public Uni<User> modifyCurrentUser(ModifyCurrentUser modifyCurrentUser) {
        return requester.request(modifyCurrentUser.asRequest()).flatMap(res -> res.as(User.class));
    }

    public Multi<Guild> getCurrentUserGuilds(GetCurrentUserGuilds getCurrentUserGuilds) {
        return requester.request(getCurrentUserGuilds.asRequest()).flatMap(res -> res.as(Guild[].class)).onItem().disjoint();
    }

    public Uni<Guild.Member> getCurrentUserGuildMember(Snowflake guildId) {
        return requester.request(new EmptyRequest("/users/@me/guilds/{guild.id}/member", Variables.variables().set("guild.id", guildId.getValueAsString())))
                .flatMap(res -> res.as(Guild.Member.class));
    }

    public Uni<Application> getCurrentBotApplicationInformation() {
        return requester.request(new EmptyRequest("/oauth2/applications/@me")).flatMap(res -> res.as(Application.class));
    }

    public abstract static class Builder<R extends Response, T extends DiscordClient<R>> {
        protected Vertx vertx;
        protected RequesterFactory<R> requesterFactory;
        protected AccessTokenSource tokenSource;
        protected GlobalRateLimiter globalRateLimiter;
        protected RateLimitStrategy<R> rateLimitStrategy;

        protected Builder(Vertx vertx, AccessTokenSource tokenSource) {
            this.vertx = vertx;
            this.tokenSource = tokenSource;
        }

        public Vertx getVertx() {
            return vertx;
        }

        public AccessTokenSource getTokenSource() {
            return tokenSource;
        }

        public Builder<R, T> requesterFactory(RequesterFactory<R> requesterFactory) {
            this.requesterFactory = requireNonNull(requesterFactory);
            return this;
        }

        public Builder<R, T> globalRateLimiter(GlobalRateLimiter globalRateLimiter) {
            this.globalRateLimiter = requireNonNull(globalRateLimiter);
            return this;
        }

        public GlobalRateLimiter getGlobalRateLimiter() {
            return globalRateLimiter;
        }

        public Builder<R, T> rateLimitStrategy(RateLimitStrategy<R> rateLimitStrategy) {
            this.rateLimitStrategy = requireNonNull(rateLimitStrategy);
            return this;
        }

        public RateLimitStrategy<R> getRateLimitStrategy() {
            return rateLimitStrategy;
        }

        public abstract T build();
    }
}
