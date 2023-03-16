package io.disquark.rest.resources.guild;

import static io.disquark.rest.util.Variables.variables;

import java.util.concurrent.Flow;

import io.disquark.immutables.ImmutableMulti;
import io.disquark.rest.request.AbstractRequestMulti;
import io.disquark.rest.request.Endpoint;
import io.disquark.rest.request.Request;
import io.disquark.rest.resources.Snowflake;
import io.vertx.core.http.HttpMethod;

import org.immutables.value.Value.Default;

@ImmutableMulti
abstract class SearchGuildMembers extends AbstractRequestMulti<Guild.Member> {

    public abstract Snowflake guildId();

    public abstract String query();

    @Default
    public int limit() {
        return 1;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Guild.Member> downstream) {
        requester().request(asRequest())
                .flatMap(res -> res.as(Guild.Member[].class))
                .onItem().<Guild.Member> disjoint()
                .subscribe().withSubscriber(downstream);
    }

    @Override
    public Request asRequest() {
        return Request.builder()
                .endpoint(Endpoint.create(HttpMethod.GET, "/guilds/{guild.id}/members/search{?query,limit}"))
                .variables(variables("guild.id", guildId().getValue(), "query", query(), "limit", limit()))
                .build();
    }
}
