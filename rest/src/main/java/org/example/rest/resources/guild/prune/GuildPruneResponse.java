package org.example.rest.resources.guild.prune;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Optional;

public class GuildPruneResponse {
    @Nullable
    private final Integer pruned;

    @JsonCreator
    public GuildPruneResponse(@JsonProperty("pruned") Integer pruned) {
        this.pruned = pruned;
    }

    public Optional<Integer> getPruned() {
        return Optional.ofNullable(pruned);
    }
}
