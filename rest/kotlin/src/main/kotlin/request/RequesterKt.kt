package io.disquark.rest.kotlin.request

import io.disquark.rest.request.Requestable
import io.disquark.rest.request.Requester
import io.disquark.rest.response.Response
import io.smallrye.mutiny.Uni

abstract class RequestKt: Requestable {
    protected abstract val requester: Requester<*>
}

internal fun <T : Response> Requester<T>.requestDeferred(requestable: Requestable): Uni<T> =
    Uni.createFrom().deferred(() -> request(requestable.asRequest()))

internal inline fun <reified T> Response.`as`(): Uni<T> = `as`(T::class.java)

@DslMarker
annotation class FooMarker

@FooMarker
abstract class Foo