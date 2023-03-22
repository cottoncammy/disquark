package io.disquark.rest.json.command;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.nullableoptional.NullableOptional;
import io.disquark.nullableoptional.jackson.NullableOptionalFilter;
import io.disquark.rest.json.Locale;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
interface GlobalApplicationCommandOverwriteJson {

    Optional<Snowflake> id();

    String name();

    @JsonProperty("name_localizations")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<Map<Locale, String>> nameLocalizations();

    Optional<String> description();

    @JsonProperty("description_localizations")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<Map<Locale, String>> descriptionLocalizations();

    Optional<List<ApplicationCommand.Option>> options();

    @JsonProperty("default_member_permissions")
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = NullableOptionalFilter.class)
    NullableOptional<EnumSet<PermissionFlag>> defaultMemberPermissions();

    Optional<ApplicationCommandJson.Type> type();

    Optional<Boolean> nsfw();

    @JsonProperty("dm_permission")
    Optional<Boolean> dmPermission();
}
