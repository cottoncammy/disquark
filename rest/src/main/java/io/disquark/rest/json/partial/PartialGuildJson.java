package io.disquark.rest.json.partial;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.guild.Guild;

@ImmutableJson
@JsonDeserialize(as = PartialGuild.class)
interface PartialGuildJson {

    Snowflake id();

    String name();

    Optional<String> icon();

    boolean owner();

    EnumSet<PermissionFlag> permissions();

    List<Guild.Feature> features();
}
