package org.example.rest.emoji;

import org.example.rest.resources.Snowflake;
import org.example.rest.resources.emoji.Emoji;

import static java.util.Objects.requireNonNull;

public interface ReactionEmoji {

    static ReactionEmoji create(Emoji emoji) {
        if (requireNonNull(emoji, "emoji").id().isPresent()) {
            return create(emoji.id().get(), emoji.name().orElseThrow(IllegalArgumentException::new));
        }
        return create(emoji.name().orElseThrow(IllegalArgumentException::new));
    }

    static CustomReactionEmoji create(Snowflake id, String name) {
        return new CustomReactionEmoji(requireNonNull(id, "id"), requireNonNull(name, "name"));
    }

    static UnicodeReactionEmoji create(String unicode) {
        return new UnicodeReactionEmoji(requireNonNull(unicode, "unicode"));
    }

    String getValue();
}
