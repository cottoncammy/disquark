package io.disquark.rest.kotlin

import io.disquark.rest.DiscordClient
import io.disquark.rest.request.HttpClientRequester
import io.disquark.rest.request.ratelimit.BucketRateLimitingRequester
import io.disquark.rest.response.Response
import io.vertx.mutiny.core.http.HttpClient

val <T : Response> DiscordClient<T>.httpClient: HttpClient?
    get() {
        return when (requester) {
            is HttpClientRequester -> (requester as HttpClientRequester).httpClient
            is BucketRateLimitingRequester -> {
                val requester = (requester as BucketRateLimitingRequester).requester
                return (requester as? HttpClientRequester)?.httpClient
            }
            else -> null
        }
    }
