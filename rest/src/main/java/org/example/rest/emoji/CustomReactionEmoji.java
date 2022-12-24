package org.example.rest.emoji;

import org.example.rest.resources.Snowflake;

public class CustomReactionEmoji implements ReactionEmoji {
    private final Snowflake id;
    private final String name;

    CustomReactionEmoji(Snowflake id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getValue() {
        return String.format("%s:%s", name, id.getValue());
    }
}
