package io.disquark.rest.kotlin.json.command

import io.disquark.rest.json.command.ApplicationCommand

@DslMarker
annotation class ApplicationCommandDslMarker

// can edit everything but type
// edit: localizations are nullable and so is permissions
// edit: name is optional

// overwrite: name is required, can specify type, id is optional

class CreateApplicationCommandDsl {
    fun chatInputCommand(name: String): {

    }

    fun userCommand(name: String) {
        // cant use options
    }

    fun messageCommand(name: String) {
        // description is required
    }
}

class ApplicationCommandWithOptionsDsl {

    fun option(type: ApplicationCommand.Option.Type) {

    }
}

class ApplicationCommandOptionDsl {

}