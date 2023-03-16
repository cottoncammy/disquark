package io.disquark.rest.resources.channel.message;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.resources.FlagEnum;
import io.disquark.rest.resources.Snowflake;
import io.disquark.rest.resources.channel.Channel;
import io.disquark.rest.resources.emoji.Emoji;
import io.disquark.rest.resources.interactions.MessageInteraction;
import io.disquark.rest.resources.interactions.components.Component;
import io.disquark.rest.resources.sticker.Sticker;
import io.disquark.rest.resources.user.User;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = Message.class)
interface MessageJson {

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

    List<Message.Attachment> attachments();

    List<Message.Embed> embeds();

    Optional<List<Message.Reaction>> reactions();

    @JsonDeserialize(using = StringDeserializer.class)
    Optional<String> nonce();

    boolean pinned();

    @JsonProperty("webhook_id")
    Optional<Snowflake> webhookId();

    Type type();

    Optional<Message.Activity> activity();

    Optional<Message.PartialApplication> application();

    @JsonProperty("application_id")
    Optional<Snowflake> applicationId();

    @JsonProperty("message_reference")
    Optional<Message.Reference> messageReference();

    Optional<EnumSet<Flag>> flags();

    @JsonProperty("referenced_message")
    Optional<Message> referencedMessage();

    Optional<MessageInteraction> interaction();

    Optional<Channel> thread();

    Optional<List<Component>> components();

    @JsonProperty("sticker_items")
    Optional<List<Sticker.Item>> stickerItems();

    Optional<List<Sticker>> stickers();

    OptionalInt position();

    enum Type {
        @JsonEnumDefaultValue
        UNKNOWN(-1),
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
    @JsonDeserialize(as = Message.Activity.class)
    interface ActivityJson {

        Type type();

        @JsonProperty("party_id")
        Optional<String> partyId();

        enum Type {
            @JsonEnumDefaultValue
            UNKNOWN(-1),
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
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.PartialApplication.class)
    interface PartialApplicationJson {

        Snowflake id();

        String name();

        Optional<String> icon();

        String description();

        @JsonProperty("cover_image")
        Optional<String> coverImage();
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
        FAILED_TO_MENTION_SOME_ROLES_IN_THREAD(8),
        SUPPRESS_NOTIFICATIONS(12);

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
    @JsonDeserialize(as = Message.Reference.class)
    interface ReferenceJson {

        @JsonProperty("message_id")
        Optional<Snowflake> messageId();

        @JsonProperty("channel_id")
        Optional<Snowflake> channelId();

        @JsonProperty("guild_id")
        Optional<Snowflake> guildId();

        @JsonProperty("fail_if_not_exists")
        Optional<Boolean> failIfNotExists();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.Reaction.class)
    interface ReactionJson {

        int count();

        boolean me();

        Emoji emoji();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.Embed.class)
    interface EmbedJson {

        Optional<String> title();

        Optional<Type> type();

        Optional<String> description();

        Optional<String> url();

        Optional<Instant> timestamp();

        OptionalInt color();

        Optional<Message.EmbedFooter> footer();

        Optional<Message.EmbedImage> image();

        Optional<Message.EmbedThumbnail> thumbnail();

        Optional<Message.EmbedVideo> video();

        Optional<Message.EmbedProvider> provider();

        Optional<Message.EmbedAuthor> author();

        Optional<List<Message.EmbedField>> fields();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.EmbedThumbnail.class)
    interface EmbedThumbnailJson {

        String url();

        @JsonProperty("proxy_url")
        Optional<String> proxyUrl();

        OptionalInt height();

        OptionalInt width();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.EmbedVideo.class)
    interface EmbedVideoJson {

        Optional<String> url();

        @JsonProperty("proxy_url")
        Optional<String> proxyUrl();

        OptionalInt height();

        OptionalInt width();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.EmbedImage.class)
    interface EmbedImageJson {

        Optional<String> url();

        @JsonProperty("proxy_url")
        Optional<String> proxyUrl();

        OptionalInt height();

        OptionalInt width();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.EmbedProvider.class)
    interface EmbedProviderJson {

        Optional<String> name();

        Optional<String> url();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.EmbedAuthor.class)
    interface EmbedAuthorJson {

        String name();

        Optional<String> url();

        @JsonProperty("icon_url")
        Optional<String> iconUrl();

        @JsonProperty("proxy_icon_url")
        Optional<String> proxyIconUrl();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.EmbedFooter.class)
    interface EmbedFooterJson {

        String text();

        @JsonProperty("icon_url")
        Optional<String> iconUrl();

        @JsonProperty("proxy_icon_url")
        Optional<String> proxyIconUrl();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.EmbedField.class)
    interface EmbedFieldJson {

        String name();

        String value();

        Optional<Boolean> inline();
    }

    @ImmutableJson
    @JsonDeserialize(as = Message.Attachment.class)
    interface AttachmentJson {

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
    }
}
