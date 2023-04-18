package io.disquark.rest.json.guild;

import java.util.Optional;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
