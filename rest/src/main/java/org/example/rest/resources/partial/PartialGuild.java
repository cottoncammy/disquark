package org.example.rest.resources.partial;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.immutables.ImmutableJson;
import org.example.rest.resources.Snowflake;
import org.example.rest.resources.guild.Guild;
import org.example.rest.resources.permissions.PermissionFlag;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

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
        protected Builder() {}
    }
}
