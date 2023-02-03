package io.disquark.it;

import io.disquark.it.config.ConfigValue;
import io.disquark.rest.DiscordBotClient;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.user.ModifyCurrentUser;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.core.http.RequestOptions;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.http.HttpClient;
import io.vertx.mutiny.core.http.HttpClientRequest;
import io.vertx.mutiny.core.http.HttpClientResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DisQuarkJUnit5ParameterResolver.class)
class UserIT {

    @Test
    void testGetUser(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.getUser(userId).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().assertCompleted();
    }

    @Test
    void testModifyCurrentUser(DiscordBotClient<?> botClient) {
        Buffer avatar = botClient.getVertx().fileSystem().readFileAndAwait("images/avatar.png");
        ModifyCurrentUser modifyCurrentUser = ModifyCurrentUser.builder().avatar(avatar).build();
        HttpClient httpClient = botClient.getVertx().createHttpClient();

        botClient.getCurrentUser()
                .call(user -> botClient.modifyCurrentUser(modifyCurrentUser))
                .call(user -> {
                    if (user.avatar().isPresent()) {
                        RequestOptions options = new RequestOptions()
                                .setAbsoluteURI(String.format("https://cdn.discordapp.com/avatars/%s/%s.png",
                                        user.id().getValueAsString(), user.avatar().get()));

                        return httpClient.request(options)
                                .flatMap(HttpClientRequest::send)
                                .flatMap(HttpClientResponse::body)
                                .call(b -> botClient.modifyCurrentUser(ModifyCurrentUser.builder().avatar(b).build()));
                    }
                    return Uni.createFrom().voidItem();
                })
                .call(httpClient::close)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertCompleted();
    }

    @Test
    void testCreateDm(DiscordBotClient<?> botClient, @ConfigValue("DISCORD_USER_ID") Snowflake userId) {
        botClient.createDm(userId).subscribe().withSubscriber(UniAssertSubscriber.create()).awaitItem().assertCompleted();
    }
}
