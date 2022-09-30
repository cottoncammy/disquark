package org.example.rest.request;

import org.example.rest.DiscordBotClient;

import java.util.function.Function;

public interface RequesterFactory extends Function<DiscordBotClient.Builder, Requester> {

    // TODO
    RequesterFactory HTTP_CLIENT_REQUESTER_FACTORY = null;
}
