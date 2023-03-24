package io.disquark.rest.kotlin

import io.disquark.rest.DiscordClient
import io.disquark.rest.request.HttpClientRequester
import io.disquark.rest.response.Response
import io.vertx.mutiny.core.http.HttpClient

val <T : Response> DiscordClient<T>.httpClient: HttpClient?
    get() = (requester as? HttpClientRequester)?.httpClient