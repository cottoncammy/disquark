package io.disquark.rest.emoji;

public class UnicodeReactionEmoji implements ReactionEmoji {
    private final String unicode;

    UnicodeReactionEmoji(String unicode) {
        this.unicode = unicode;
    }

    @Override
    public String getValue() {
        return unicode;
    }
}
