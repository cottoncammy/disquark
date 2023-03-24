package io.disquark.rest.kotlin.json.command

import io.disquark.rest.json.Locale
import io.disquark.rest.json.PermissionFlag
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.command.ApplicationCommand
import io.disquark.rest.request.Requester
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

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

sealed class CreateApplicationCommand(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    protected val name: String,
): ApplicationCommandDsl() {
    protected abstract val type: ApplicationCommand.Type?

    protected open val _guildId: Snowflake? = null
    protected open var dmPermission: Boolean? = null

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

sealed class CreateApplicationCommandWithOptions(requester: Requester<*>, applicationId: Snowflake, name: String):
    CreateApplicationCommand(requester, applicationId, name), ApplicationCommandOptionsDsl {

    override var options: MutableList<ApplicationCommandOption>? = null
}

sealed class CreateChatInputCommand(requester: Requester<*>, applicationId: Snowflake, name: String):
    CreateApplicationCommandWithOptions(requester, applicationId, name) {

    override val type: ApplicationCommand.Type? = ApplicationCommand.Type.CHAT_INPUT
}

sealed class CreateUserCommand(requester: Requester<*>, applicationId: Snowflake, name: String):
    CreateApplicationCommand(requester, applicationId, name) {

    override val type: ApplicationCommand.Type? = ApplicationCommand.Type.USER
}

sealed class CreateMessageCommand(requester: Requester<*>, applicationId: Snowflake, name: String):
    CreateApplicationCommand(requester, applicationId, name) {

    override val type: ApplicationCommand.Type? = ApplicationCommand.Type.MESSAGE
}

class CreateGlobalChatInputCommand(requester: Requester<*>, applicationId: Snowflake, name: String): CreateChatInputCommand(requester, applicationId, name) {
    public override var dmPermission: Boolean? = null
}

class CreateGlobalUserCommand(requester: Requester<*>, applicationId: Snowflake, name: String): CreateUserCommand(requester, applicationId, name) {
    public override var dmPermission: Boolean? = null
}

class CreateGlobalMessageCommand(requester: Requester<*>, applicationId: Snowflake, name: String): CreateMessageCommand(requester, applicationId, name) {
    public override var dmPermission: Boolean? = null
}

class CreateGuildChatInputCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake, name: String): CreateChatInputCommand(requester, applicationId, name) {
    override val _guildId: Snowflake?
        get() = guildId
}

class CreateGuildUserCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake, name: String): CreateUserCommand(requester, applicationId, name) {
    override val _guildId: Snowflake?
        get() = guildId
}

class CreateGuildMessageCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake?, name: String): CreateMessageCommand(requester, applicationId, name) {
    override val _guildId: Snowflake?
        get() = guildId
}

sealed class EditApplicationCommandDsl(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    private val commandId: Snowflake,
): ApplicationCommandDsl(), ApplicationCommandOptionsDsl {
    protected open val _guildId: Snowflake? = null
    protected open var dmPermission: Boolean? = null

    override var options: MutableList<ApplicationCommandOption>? = null

    var name: String? = null

    var nameLocalizations: Optional<MutableMap<Locale, String>>?
        get() = _nameLocalizations
        set(value) {
            _nameLocalizations = nameLocalizations
        }

    var descriptionLocalizations: Optional<MutableMap<Locale, String>>?
        get() = _descriptionLocalizations
        set(value) {
            _descriptionLocalizations = descriptionLocalizations
        }
}

class EditGlobalApplicationCommand(requester: Requester<*>, applicationId: Snowflake, commandId: Snowflake):
    EditApplicationCommandDsl(requester, applicationId, commandId) {

    public override var dmPermission: Boolean? = null
}

class EditGuildApplicationCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake, commandId: Snowflake):
    EditApplicationCommandDsl(requester, applicationId, commandId) {

    override val _guildId: Snowflake?
        get() = guildId
}

sealed class BulkOverwriteApplicationCommandDsl(private val requester: Requester<*>, private val applicationId: Snowflake) {
    protected abstract val _guildId: Snowflake?

    var _commandOverwrites = MutableList<>

    private fun
}
