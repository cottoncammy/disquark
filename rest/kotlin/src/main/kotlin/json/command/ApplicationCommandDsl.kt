package io.disquark.rest.kotlin.json.command

import io.disquark.rest.json.Locale
import io.disquark.rest.json.PermissionFlag
import io.disquark.rest.json.Snowflake
import io.disquark.rest.json.command.*
import io.disquark.rest.kotlin.nullableoptional.toNullableOptional
import io.disquark.rest.request.Requester
import java.util.EnumSet
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@DslMarker
annotation class ApplicationCommandDslMarker

typealias DefaultMemberPermission = PermissionFlag

@ApplicationCommandDslMarker
sealed class ApplicationCommandLocalizationsDsl {
    var nameLocalizations: Optional<MutableMap<Locale, String>>? = Optional.empty()
    var descriptionLocalizations: Optional<MutableMap<Locale, String>>? = Optional.empty()

    private val _nameLocalizations: MutableMap<Locale, String>
        get() = nameLocalizations?.getOrNull() ?: mutableMapOf<Locale, String>().also { nameLocalizations = Optional.of(it) }

    private val _descriptionLocalizations: MutableMap<Locale, String>
        get() = descriptionLocalizations?.getOrNull() ?: mutableMapOf<Locale, String>().also { descriptionLocalizations = Optional.of(it) }

    fun nameLocalizations(init: MutableMap<Locale, String>.() -> Unit) {
        _nameLocalizations + mutableMapOf<Locale, String>().apply(init)
    }

    fun descriptionLocalizations(init: MutableMap<Locale, String>.() -> Unit) {
        _descriptionLocalizations + mutableMapOf<Locale, String>().apply(init)
    }
}

sealed class ApplicationCommandDsl(var description: String? = null, var nsfw: Boolean? = null) : ApplicationCommandLocalizationsDsl() {
    var defaultMemberPermissions: Optional<MutableSet<PermissionFlag>>? = Optional.empty()

    private val _defaultMemberPermissions: MutableSet<PermissionFlag>
        get() = defaultMemberPermissions?.getOrNull() ?: mutableSetOf<PermissionFlag>().also { defaultMemberPermissions = Optional.of(it) }

    operator fun DefaultMemberPermission.unaryPlus() {
        _defaultMemberPermissions + this
    }
}

interface ApplicationCommandOptionsDsl {
    var options: MutableList<ApplicationCommandOption>?

    operator fun ApplicationCommandOption.unaryPlus() {
        (options ?: mutableListOf()) + this
    }

    fun stringOption(name: String, description: String, init: StringOption.() -> Unit) {
        +StringOption(name, description).apply(init)
    }

    fun intOption(name: String, description: String, init: IntOption.() -> Unit) {
        +IntOption(name, description).apply(init)
    }

    fun booleanOption(name: String, description: String, init: ApplicationCommandOption.() -> Unit) {
        +ApplicationCommandOption(ApplicationCommand.Option.Type.BOOLEAN, name, description).apply(init)
    }

    fun userOption(name: String, description: String, init: ApplicationCommandOption.() -> Unit) {
        +ApplicationCommandOption(ApplicationCommand.Option.Type.USER, name, description).apply(init)
    }

    fun channelOption(name: String, description: String, init: ChannelOption.() -> Unit) {
        +ChannelOption(name, description).apply(init)
    }

    fun roleOption(name: String, description: String, init: ApplicationCommandOption.() -> Unit) {
        +ApplicationCommandOption(ApplicationCommand.Option.Type.ROLE, name, description).apply(init)
    }

    fun mentionableOption(name: String, description: String, init: ApplicationCommandOption.() -> Unit) {
        +ApplicationCommandOption(ApplicationCommand.Option.Type.MENTIONABLE, name, description).apply(init)
    }

    fun doubleOption(name: String, description: String, init: DoubleOption.() -> Unit) {
        +DoubleOption(name, description).apply(init)
    }

    fun attachmentOption(name: String, description: String, init: ApplicationCommandOption.() -> Unit) {
        +ApplicationCommandOption(ApplicationCommand.Option.Type.ATTACHMENT, name, description).apply(init)
    }
}

interface ApplicationCommandOptionsWithSubcommandDsl : ApplicationCommandOptionsDsl {
    fun subcommand(name: String, description: String, init: SubcommandOption.() -> Unit) {
        +SubcommandOption(name, description).apply(init)
    }
}

interface ApplicationCommandOptionsWithSubcommandGroupDsl : ApplicationCommandOptionsWithSubcommandDsl {
    fun subcommandGroup(name: String, description: String, init: SubcommandGroupOption.() -> Unit) {
        +SubcommandGroupOption(name, description).apply(init)
    }
}

sealed class CreateApplicationCommand(
    private val requester: Requester<*>,
    private val applicationId: Snowflake,
    protected val name: String,
) : ApplicationCommandDsl() {
    protected abstract val type: ApplicationCommand.Type?
    protected open val _guildId: Snowflake? = null
    protected open var dmPermission: Boolean? = null

    protected open fun toGlobalCommandUni(): CreateGlobalApplicationCommandUni {
        return CreateGlobalApplicationCommandUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .name(name)
            .nameLocalizations(nameLocalizations?.getOrNull())
            .description(description)
            .descriptionLocalizations(descriptionLocalizations?.getOrNull())
            .defaultMemberPermissions(defaultMemberPermissions?.map { EnumSet.copyOf(it) }?.getOrNull())
            .dmPermission(dmPermission)
            .type(type)
            .nsfw(nsfw)
            .build()
    }

    internal open fun toGlobalCommandOverwrite(): GlobalApplicationCommandOverwrite {
        return GlobalApplicationCommandOverwrite(name)
            .withNameLocalizations(nameLocalizations.toNullableOptional())
            .run { description?.let { withDescription(it) } ?: this }
            .withDescriptionLocalizations(descriptionLocalizations.toNullableOptional())
            .withDefaultMemberPermissions(defaultMemberPermissions?.map { EnumSet.copyOf(it) }.toNullableOptional())
            .run { type?.let { withType(it) } ?: this }
            .run { nsfw?.let { withNsfw(it) } ?: this }
            .run { dmPermission?.let { withDmPermission(it) } ?: this }
    }

    protected open fun toGuildCommandUni(): CreateGuildApplicationCommandUni {
        return CreateGuildApplicationCommandUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .apply { _guildId?.let { guildId(it) } }
            .name(name)
            .nameLocalizations(nameLocalizations?.getOrNull())
            .description(description)
            .descriptionLocalizations(descriptionLocalizations?.getOrNull())
            .defaultMemberPermissions(defaultMemberPermissions?.map { EnumSet.copyOf(it) }?.getOrNull())
            .type(type)
            .nsfw(nsfw)
            .build()
    }

    internal open fun toGuildCommandOverwrite(): GuildApplicationCommandOverwrite {
        return GuildApplicationCommandOverwrite(name)
            .withNameLocalizations(nameLocalizations.toNullableOptional())
            .run { description?.let { withDescription(it) } ?: this }
            .withDescriptionLocalizations(descriptionLocalizations.toNullableOptional())
            .withDefaultMemberPermissions(defaultMemberPermissions?.map { EnumSet.copyOf(it) }.toNullableOptional())
            .run { type?.let { withType(it) } ?: this }
            .run { nsfw?.let { withNsfw(it) } ?: this }
    }
}

sealed class CreateApplicationCommandWithOptions(requester: Requester<*>, applicationId: Snowflake, name: String) :
    CreateApplicationCommand(requester, applicationId, name), ApplicationCommandOptionsDsl {

    override var options: MutableList<ApplicationCommandOption>? = null

    override fun toGlobalCommandUni(): CreateGlobalApplicationCommandUni {
        return CreateGlobalApplicationCommandUni.builder()
            .from(super.toGlobalCommandUni())
            .options(options?.map { it.toImmutable() })
            .build()
    }

    override fun toGlobalCommandOverwrite(): GlobalApplicationCommandOverwrite {
        return super.toGlobalCommandOverwrite()
            .run { options?.let { it -> withOptions(it.map { it.toImmutable() }) } ?: this }
    }

    override fun toGuildCommandUni(): CreateGuildApplicationCommandUni {
        return CreateGuildApplicationCommandUni.builder()
            .from(super.toGuildCommandUni())
            .options(options?.map { it.toImmutable() })
            .build()
    }

    override fun toGuildCommandOverwrite(): GuildApplicationCommandOverwrite {
        return super.toGuildCommandOverwrite()
            .run { options?.let { it -> withOptions(it.map { it.toImmutable() }) } ?: this }
    }
}

sealed class CreateChatInputCommand(requester: Requester<*>, applicationId: Snowflake, name: String) :
    CreateApplicationCommandWithOptions(requester, applicationId, name) {

    override val type: ApplicationCommand.Type? = ApplicationCommand.Type.CHAT_INPUT
}

sealed class CreateUserCommand(requester: Requester<*>, applicationId: Snowflake, name: String) :
    CreateApplicationCommand(requester, applicationId, name) {

    override val type: ApplicationCommand.Type? = ApplicationCommand.Type.USER
}

sealed class CreateMessageCommand(requester: Requester<*>, applicationId: Snowflake, name: String) :
    CreateApplicationCommand(requester, applicationId, name) {

    override val type: ApplicationCommand.Type? = ApplicationCommand.Type.MESSAGE
}

class CreateGlobalChatInputCommand(requester: Requester<*>, applicationId: Snowflake, name: String) : CreateChatInputCommand(requester, applicationId, name) {
    public override var dmPermission: Boolean? = null

    internal fun toUni(): CreateGlobalApplicationCommandUni = toGlobalCommandUni()
}

class CreateGlobalUserCommand(requester: Requester<*>, applicationId: Snowflake, name: String) : CreateUserCommand(requester, applicationId, name) {
    public override var dmPermission: Boolean? = null

    internal fun toUni(): CreateGlobalApplicationCommandUni = toGlobalCommandUni()
}

class CreateGlobalMessageCommand(requester: Requester<*>, applicationId: Snowflake, name: String) : CreateMessageCommand(requester, applicationId, name) {
    public override var dmPermission: Boolean? = null

    internal fun toUni(): CreateGlobalApplicationCommandUni = toGlobalCommandUni()
}

class CreateGuildChatInputCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake, name: String) : CreateChatInputCommand(requester, applicationId, name) {
    override val _guildId: Snowflake?
        get() = guildId

    internal fun toUni(): CreateGuildApplicationCommandUni = toGuildCommandUni()
}

class CreateGuildUserCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake, name: String) : CreateUserCommand(requester, applicationId, name) {
    override val _guildId: Snowflake?
        get() = guildId

    internal fun toUni(): CreateGuildApplicationCommandUni = toGuildCommandUni()
}

class CreateGuildMessageCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake?, name: String) : CreateMessageCommand(requester, applicationId, name) {
    override val _guildId: Snowflake?
        get() = guildId

    internal fun toUni(): CreateGuildApplicationCommandUni = toGuildCommandUni()
}

sealed class EditApplicationCommandDsl(
    protected val requester: Requester<*>,
    protected val applicationId: Snowflake,
    protected val commandId: Snowflake,
    var name: String? = null,
) : ApplicationCommandDsl(), ApplicationCommandOptionsWithSubcommandGroupDsl {
    override var options: MutableList<ApplicationCommandOption>? = null

    protected open var dmPermission: Boolean? = null
}

class EditGlobalApplicationCommand(requester: Requester<*>, applicationId: Snowflake, commandId: Snowflake) :
    EditApplicationCommandDsl(requester, applicationId, commandId) {

    public override var dmPermission: Boolean? = null

    internal fun toUni(): EditGlobalApplicationCommandUni {
        return EditGlobalApplicationCommandUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .commandId(commandId)
            .name(name)
            .nameLocalizations(nameLocalizations.toNullableOptional())
            .description(description)
            .descriptionLocalizations(descriptionLocalizations.toNullableOptional())
            .options(options?.map { it.toImmutable() })
            .defaultMemberPermissions(defaultMemberPermissions?.map { EnumSet.copyOf(it) }.toNullableOptional())
            .dmPermission(dmPermission)
            .nsfw(nsfw)
            .build()
    }
}

class EditGuildApplicationCommand(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake, commandId: Snowflake) :
    EditApplicationCommandDsl(requester, applicationId, commandId) {

    internal fun toUni(): EditGuildApplicationCommandUni {
        return EditGuildApplicationCommandUni.builder()
            .requester(requester)
            .applicationId(applicationId)
            .commandId(commandId)
            .guildId(guildId)
            .name(name)
            .name(name)
            .nameLocalizations(nameLocalizations.toNullableOptional())
            .description(description)
            .descriptionLocalizations(descriptionLocalizations.toNullableOptional())
            .options(options?.map { it.toImmutable() })
            .defaultMemberPermissions(defaultMemberPermissions?.map { EnumSet.copyOf(it) }.toNullableOptional())
            .nsfw(nsfw)
            .build()
    }
}

sealed class BulkOverwriteApplicationCommandDsl(protected val requester: Requester<*>, protected val applicationId: Snowflake) {
    var overwrites: MutableList<CreateApplicationCommand> = mutableListOf()

    protected fun <T : CreateChatInputCommand> chatInputCommand(t: T) {
        overwrites + t
    }

    protected fun <T : CreateUserCommand> userCommand(t: T) {
        overwrites + t
    }

    protected fun <T : CreateMessageCommand> messageCommand(t: T) {
        overwrites + t
    }
}

class BulkOverwriteGlobalApplicationCommands(requester: Requester<*>, applicationId: Snowflake) :
    BulkOverwriteApplicationCommandDsl(requester, applicationId) {

    fun chatInputCommand(name: String, init: CreateGlobalChatInputCommand.() -> Unit) {
        chatInputCommand(CreateGlobalChatInputCommand(requester, applicationId, name).apply(init))
    }

    fun userCommand(name: String, init: CreateGlobalUserCommand.() -> Unit) {
        userCommand(CreateGlobalUserCommand(requester, applicationId, name).apply(init))
    }

    fun messageCommand(name: String, init: CreateGlobalMessageCommand.() -> Unit) {
        messageCommand(CreateGlobalMessageCommand(requester, applicationId, name).apply(init))
    }

    internal fun toMulti(): BulkOverwriteGlobalApplicationCommandsMulti {
        return BulkOverwriteGlobalApplicationCommandsMulti.builder()
            .requester(requester)
            .applicationId(applicationId)
            .overwrites(overwrites.map { (it as? CreateGlobalChatInputCommand)?.toGlobalCommandOverwrite() ?: it.toGlobalCommandOverwrite() })
            .build()
    }
}

class BulkOverwriteGuildApplicationCommands(requester: Requester<*>, applicationId: Snowflake, private val guildId: Snowflake) :
    BulkOverwriteApplicationCommandDsl(requester, applicationId) {

    fun chatInputCommand(name: String, init: CreateGuildChatInputCommand.() -> Unit) {
        chatInputCommand(CreateGuildChatInputCommand(requester, applicationId, guildId, name).apply(init))
    }

    fun userCommand(name: String, init: CreateGuildUserCommand.() -> Unit) {
        userCommand(CreateGuildUserCommand(requester, applicationId, guildId, name).apply(init))
    }

    fun messageCommand(name: String, init: CreateGuildMessageCommand.() -> Unit) {
        messageCommand(CreateGuildMessageCommand(requester, applicationId, guildId, name).apply(init))
    }

    internal fun toMulti(): BulkOverwriteGuildApplicationCommandsMulti {
        return BulkOverwriteGuildApplicationCommandsMulti.builder()
            .requester(requester)
            .applicationId(applicationId)
            .guildId(guildId)
            .overwrites(overwrites.map { (it as? CreateGuildChatInputCommand)?.toGuildCommandOverwrite() ?: it.toGuildCommandOverwrite() })
            .build()
    }
}
