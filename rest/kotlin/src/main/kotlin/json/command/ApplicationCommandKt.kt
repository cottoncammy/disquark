package io.disquark.rest.kotlin.json.command

import io.disquark.rest.json.command.ApplicationCommand

fun ApplicationCommand.Option.subcommand(init: ApplicationCommand.Option.() -> Unit)

fun ApplicationCommand.Option.subcommandGroup(init: ApplicationCommand.Option.() -> Unit)