package org.example.rest.resources.guild;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModifyMfaLevelResponse {
    private final Guild.MfaLevel level;

    public ModifyMfaLevelResponse(@JsonProperty Guild.MfaLevel level) {
        this.level = level;
    }

    public Guild.MfaLevel getLevel() {
        return level;
    }
}
