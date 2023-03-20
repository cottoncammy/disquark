package io.disquark.rest.json.guild;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.PermissionFlag;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
@JsonDeserialize(as = CurrentUserGuild.class)
interface CurrentUserGuildJson {

    Snowflake id();

    String name();

    Optional<String> icon();

    boolean owner();

    EnumSet<PermissionFlag> permissions();

    List<Guild.Feature> features();
}
