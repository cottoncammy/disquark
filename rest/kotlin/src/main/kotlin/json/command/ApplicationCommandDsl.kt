package io.disquark.rest.kotlin.json.command

import io.disquark.rest.json.command.ApplicationCommand

sealed class ApplicationCommandRequest {
    fun option(init: ApplicationCommand.Option.() -> Unit) = TODO()

    fun
}