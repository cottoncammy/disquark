package io.disquark.rest.resources.partial;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.guild.Guild;
import io.disquark.rest.resources.permissions.PermissionFlag;

@ImmutableJson
@JsonDeserialize(as = ImmutablePartialGuild.class)
public interface PartialGuild {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    String name();

    Optional<String> icon();

    boolean owner();

    EnumSet<PermissionFlag> permissions();

    List<Guild.Feature> features();

    class Builder extends ImmutablePartialGuild.Builder {
        protected Builder() {
        }
    }
}
