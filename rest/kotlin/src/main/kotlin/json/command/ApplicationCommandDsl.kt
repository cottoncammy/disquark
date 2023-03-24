package io.disquark.rest.kotlin.json.command

import io.disquark.rest.json.Locale
import io.disquark.rest.json.PermissionFlag
import io.disquark.rest.json.command.ApplicationCommand
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

// TODO message flags unary plus

@DslMarker
annotation class ApplicationCommandDslMarker

data class NameLocalization(val locale: Locale, val name: String)

data class DescriptionLocalization(val locale: Locale, val description: String)

typealias DefaultMemberPermission = PermissionFlag

@ApplicationCommandDslMarker
sealed class ApplicationCommandLocalizationsDsl {
    protected var _nameLocalizations: Optional<MutableMap<Locale, String>>? = Optional.empty()
    protected var _descriptionLocalizations: Optional<MutableMap<Locale, String>>? = Optional.empty()

    private val nameLocalizations: MutableMap<Locale, String>
        get() = _nameLocalizations?.getOrNull() ?: mutableMapOf()

    private val descriptionLocalizations: MutableMap<Locale, String>
        get() = _descriptionLocalizations?.getOrNull() ?: mutableMapOf()

    operator fun NameLocalization.unaryPlus() {
        nameLocalizations += locale to name
    }

    operator fun DescriptionLocalization.unaryPlus() {
        descriptionLocalizations += locale to description
    }
}

sealed class ApplicationCommandDsl: ApplicationCommandLocalizationsDsl() {
    protected var _defaultMemberPermissions: Optional<MutableSet<PermissionFlag>>? = Optional.empty()

    private val defaultMemberPermissions: MutableSet<PermissionFlag>
        get() = _defaultMemberPermissions?.getOrNull() ?: mutableSetOf()

    var description: String? = null
    var nsfw: Boolean? = null

    operator fun DefaultMemberPermission.unaryPlus() {
        defaultMemberPermissions += this
    }
}

interface ApplicationCommandOptionsDsl {
    var options: MutableList<ApplicationCommandOption>?

    fun option(init: ApplicationCommandOption.() -> Unit) {
        (options ?: mutableListOf()) += ApplicationCommandOption().apply(init)
    }
}

sealed class CreateApplicationCommand(val name: String): ApplicationCommandDsl() {
    protected abstract var type: ApplicationCommand.Type?

    var nameLocalizations: MutableMap<Locale, String>?
        get() = _nameLocalizations?.getOrNull()
        set(value) {
            _nameLocalizations = Optional.ofNullable(value)
        }

    var descriptionLocalizations: MutableMap<Locale, String>?
        get() = _descriptionLocalizations?.getOrNull()
        set(value) {
            _descriptionLocalizations = Optional.ofNullable(value)
        }
}

sealed class CreateApplicationCommandWithOptions(name: String): CreateApplicationCommand(name), ApplicationCommandOptionsDsl {
}

interface EditApplicationCommandDsl {
    var name: String? = null
}

class OverwriteApplicationCommandDsl {
    val name: String
}
