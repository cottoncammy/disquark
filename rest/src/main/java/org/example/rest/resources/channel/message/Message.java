package org.example.rest.resources.channel.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.rest.jackson.NonceDeserializer;
import org.example.rest.resources.Snowflake;
import org.example.rest.immutables.ImmutableJson;
import org.example.rest.resources.application.Application;
import org.example.rest.resources.channel.Channel;
import org.example.rest.resources.emoji.Emoji;
import org.example.rest.resources.interactions.Interaction;
import org.example.rest.resources.interactions.components.Component;
import org.example.rest.resources.sticker.Sticker;
import org.example.rest.resources.user.User;
import org.example.rest.resources.FlagEnum;
import org.immutables.value.Value.Enclosing;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = ImmutableMessage.class)
public interface Message {

    static Builder builder() {
        return new Builder();
    }

    Snowflake id();

    @JsonProperty("channel_id")
    Snowflake channelId();

    User author();

    String content();

    Instant timestamp();

    @JsonProperty("edited_timestamp")
    Optional<Instant> editedTimestamp();

    boolean tts();

    @JsonProperty("mention_everyone")
    boolean mentionEveryone();

    List<User> mentions();

    @JsonProperty("mention_roles")
    List<Snowflake> mentionRoles();

    @JsonProperty("mention_channels")
    Optional<List<Channel.Mention>> mentionChannels();

    List<Attachment> attachments();

    List<Embed> embeds();

    Optional<List<Reaction>> reactions();

    @JsonDeserialize(using = NonceDeserializer.class)
    Optional<String> nonce();

    boolean pinned();

    @JsonProperty("webhook_id")
    Optional<Snowflake> webhookId();

    Type type();

    Optional<Activity> activity();

    Optional<Application> application();

    @JsonProperty("application_id")
    Optional<Snowflake> applicationId();

    @JsonProperty("message_reference")
    Optional<Reference> messageReference();

    Optional<EnumSet<Flag>> flags();

    @JsonProperty("referenced_message")
    Optional<Message> referencedMessage();

    Optional<Interaction> interaction();

    Optional<Channel> thread();

    Optional<List<Component>> components();

    @JsonProperty("sticker_items")
    Optional<List<Sticker.Item>> stickerItems();

    Optional<List<Sticker>> stickers();

    OptionalInt position();

    enum Type {
        DEFAULT(0),
        RECIPIENT_ADD(1),
        RECIPIENT_REMOVE(2),
        CALL(3),
        CHANNEL_NAME_CHANGE(4),
        CHANNEL_ICON_CHANGE(5),
        CHANNEL_PINNED_MESSAGE(6),
        USER_JOIN(7),
        GUILD_BOOST(8),
        GUILD_BOOST_TIER_1(9),
        GUILD_BOOST_TIER_2(10),
        GUILD_BOOST_TIER_3(11),
        CHANNEL_FOLLOW_ADD(12),
        GUILD_DISCOVERY_DISQUALIFIED(14),
        GUILD_DISCOVERY_REQUALIFIED(15),
        GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16),
        GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17),
        THREAD_CREATED(18),
        REPLY(19),
        CHAT_INPUT_COMMAND(20),
        THREAD_STARTER_MESSAGE(21),
        GUILD_INVITE_REMINDER(22),
        CONTEXT_MENU_COMMAND(23),
        AUTO_MODERATION_ACTION(24);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableMessage.Activity.class)
    interface Activity {

        static Builder builder() {
            return new Builder();
        }

        Type type();

        @JsonProperty("party_id")
        Optional<String> partyId();

        enum Type {
            JOIN(1),
            SPECTATE(2),
            LISTEN(3),
            JOIN_REQUEST(5);

            private final int value;

            Type(int value) {
                this.value = value;
            }

            @JsonValue
            public int getValue() {
                return value;
            }
        }

        class Builder extends ImmutableMessage.Activity.Builder {
            protected Builder() {}
        }
    }

    enum Flag implements FlagEnum {
        CROSSPOSTED(0),
        IS_CROSSPOST(1),
        SUPPRESS_EMBEDS(2),
        SOURCE_MESSAGE_DELETED(3),
        URGENT(4),
        HAS_THREAD(5),
        EPHEMERAL(6),
        LOADING(7),
        FAILED_TO_MENTION_SOME_ROLES_IN_THREAD(8);

        private final int value;

        Flag(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    @JsonDeserialize(as = ImmutableMessage.Reference.class)
    interface Reference {

        static Builder builder() {
            return new Builder();
        }

        @JsonProperty("message_id")
        Optional<Snowflake> messageId();

        @JsonProperty("channel_id")
        Optional<Snowflake> channelId();

        @JsonProperty("guild_id")
        Optional<Snowflake> guildId();

        @JsonProperty("fail_if_not_exists")
        Optional<Boolean> failIfNotExists();

        class Builder extends ImmutableMessage.Reference.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonDeserialize(as = ImmutableMessage.Reaction.class)
    interface Reaction {

        static Builder builder() {
            return new Builder();
        }

        int count();

        boolean me();

        Emoji emoji();

        class Builder extends ImmutableMessage.Reaction.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    @JsonDeserialize(as = ImmutableMessage.Embed.class)
    interface Embed {

        static Builder builder() {
            return new Builder();
        }

        Optional<String> title();

        Optional<Type> type();

        Optional<String> description();

        Optional<String> url();

        Optional<Instant> timestamp();

        OptionalInt color();

        Optional<Footer> footer();

        Optional<Image> image();

        Optional<Thumbnail> thumbnail();

        Optional<Video> video();

        Optional<Provider> provider();

        Optional<Author> author();

        Optional<List<Field>> fields();

        @ImmutableJson
        @JsonInclude(Include.NON_ABSENT)
        @JsonDeserialize(as = ImmutableMessage.Thumbnail.class)
        interface Thumbnail {

            static Builder builder() {
                return new Builder();
            }

            String url();

            @JsonProperty("proxy_url")
            Optional<String> proxyUrl();

            OptionalInt height();

            OptionalInt width();

            class Builder extends ImmutableMessage.Thumbnail.Builder {
                protected Builder() {}
            }
        }

        @ImmutableJson
        @JsonInclude(Include.NON_ABSENT)
        @JsonDeserialize(as = ImmutableMessage.Video.class)
        interface Video {

            static Builder builder() {
                return new Builder();
            }

            Optional<String> url();

            @JsonProperty("proxy_url")
            Optional<String> proxyUrl();

            OptionalInt height();

            OptionalInt width();

            class Builder extends ImmutableMessage.Video.Builder {
                protected Builder() {}
            }
        }

        @ImmutableJson
        @JsonInclude(Include.NON_ABSENT)
        @JsonDeserialize(as = ImmutableMessage.Image.class)
        interface Image {

            static Builder builder() {
                return new Builder();
            }

            Optional<String> url();

            @JsonProperty("proxy_url")
            Optional<String> proxyUrl();

            OptionalInt height();

            OptionalInt width();

            class Builder extends ImmutableMessage.Image.Builder {
                protected Builder() {}
            }
        }

        @ImmutableJson
        @JsonInclude(Include.NON_ABSENT)
        @JsonDeserialize(as = ImmutableMessage.Provider.class)
        interface Provider {

            static Builder builder() {
                return new Builder();
            }

            Optional<String> name();

            Optional<String> url();

            class Builder extends ImmutableMessage.Provider.Builder {
                protected Builder() {}
            }
        }

        @ImmutableJson
        @JsonInclude(Include.NON_ABSENT)
        @JsonDeserialize(as = ImmutableMessage.Author.class)
        interface Author {

            static Builder builder() {
                return new Builder();
            }

            String name();

            Optional<String> url();

            @JsonProperty("icon_url")
            Optional<String> iconUrl();

            @JsonProperty("proxy_icon_url")
            Optional<String> proxyIconUrl();

            class Builder extends ImmutableMessage.Author.Builder {
                protected Builder() {}
            }
        }

        @ImmutableJson
        @JsonInclude(Include.NON_ABSENT)
        @JsonDeserialize(as = ImmutableMessage.Footer.class)
        interface Footer {

            static Builder builder() {
                return new Builder();
            }

            String text();

            @JsonProperty("icon_url")
            Optional<String> iconUrl();

            @JsonProperty("proxy_icon_url")
            Optional<String> proxyIconUrl();

            class Builder extends ImmutableMessage.Footer.Builder {
                protected Builder() {}
            }
        }

        @ImmutableJson
        @JsonInclude(Include.NON_ABSENT)
        @JsonDeserialize(as = ImmutableMessage.Field.class)
        interface Field {

            static Builder builder() {
                return new Builder();
            }

            String name();

            String value();

            Optional<Boolean> inline();

            class Builder extends ImmutableMessage.Field.Builder {
                protected Builder() {}
            }
        }

        class Builder extends ImmutableMessage.Embed.Builder {
            protected Builder() {}
        }
    }

    @ImmutableJson
    @JsonInclude(Include.NON_ABSENT)
    @JsonDeserialize(as = ImmutableMessage.Attachment.class)
    interface Attachment {

        static Builder builder() {
            return new Builder();
        }

        Snowflake id();

        String filename();

        Optional<String> description();

        @JsonProperty("content_type")
        Optional<String> contentType();

        int size();

        String url();

        @JsonProperty("proxy_url")
        String proxyUrl();

        OptionalInt height();

        OptionalInt width();

        Optional<Boolean> ephemeral();

        class Builder extends ImmutableMessage.Attachment.Builder {
            protected Builder() {}
        }
    }

    class Builder extends ImmutableMessage.Builder {
        protected Builder() {}
    }
}
