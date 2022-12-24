package org.example.rest.resources.guild.prune;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.annotation.Nullable;
import java.util.Optional;

public class GuildPruneResult {
    @Nullable
    private final Integer pruned;

    @JsonCreator
    public static GuildPruneResult create(Integer pruned) {
        return new GuildPruneResult(pruned);
    }

    private GuildPruneResult(Integer pruned) {
        this.pruned = pruned;
    }

    public Optional<Integer> getPruned() {
        return Optional.ofNullable(pruned);
    }
}
