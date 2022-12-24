package org.example.rest.emoji;

import org.example.rest.resources.Snowflake;
import org.example.rest.resources.emoji.Emoji;

public interface ReactionEmoji {

    static ReactionEmoji create(Emoji emoji) {
        if (emoji.id().isPresent()) {
            return createCustomEmoji(emoji.id().get(), emoji.name().orElseThrow(IllegalArgumentException::new));
        }
        return createUnicodeEmoji(emoji.name().orElseThrow(IllegalArgumentException::new));
    }

    static CustomReactionEmoji createCustomEmoji(Snowflake id, String name) {
        return new CustomReactionEmoji(id, name);
    }

    static UnicodeReactionEmoji createUnicodeEmoji(String unicode) {
        return new UnicodeReactionEmoji(unicode);
    }

    String getValue();
}
