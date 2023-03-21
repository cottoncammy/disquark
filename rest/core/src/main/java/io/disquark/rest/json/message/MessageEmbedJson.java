package io.disquark.rest.json.message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.disquark.immutables.ImmutableJson;

import org.immutables.value.Value.Enclosing;

@Enclosing
@ImmutableJson
@JsonDeserialize(as = MessageEmbed.class)
interface MessageEmbedJson {

    Optional<String> title();

    Optional<String> description();

    Optional<String> url();

    Optional<Instant> timestamp();

    OptionalInt color();

    Optional<MessageEmbed.Footer> footer();

    Optional<MessageEmbed.Image> image();

    Optional<MessageEmbed.Thumbnail> thumbnail();

    Optional<MessageEmbed.Video> video();

    Optional<MessageEmbed.Provider> provider();

    Optional<MessageEmbed.Author> author();

    Optional<List<MessageEmbed.Field>> fields();

    @ImmutableJson
    @JsonDeserialize(as = MessageEmbed.Thumbnail.class)
    interface ThumbnailJson {

        String url();

        @JsonProperty("proxy_url")
        Optional<String> proxyUrl();

        OptionalInt height();

        OptionalInt width();
    }

    @ImmutableJson
    @JsonDeserialize(as = MessageEmbed.Video.class)
    interface VideoJson {

        Optional<String> url();

        @JsonProperty("proxy_url")
        Optional<String> proxyUrl();

        OptionalInt height();

        OptionalInt width();
    }

    @ImmutableJson
    @JsonDeserialize(as = MessageEmbed.Image.class)
    interface ImageJson {

        Optional<String> url();

        @JsonProperty("proxy_url")
        Optional<String> proxyUrl();

        OptionalInt height();

        OptionalInt width();
    }

    @ImmutableJson
    @JsonDeserialize(as = MessageEmbed.Provider.class)
    interface ProviderJson {

        Optional<String> name();

        Optional<String> url();
    }

    @ImmutableJson
    @JsonDeserialize(as = MessageEmbed.Author.class)
    interface AuthorJson {

        String name();

        Optional<String> url();

        @JsonProperty("icon_url")
        Optional<String> iconUrl();

        @JsonProperty("proxy_icon_url")
        Optional<String> proxyIconUrl();
    }

    @ImmutableJson
    @JsonDeserialize(as = MessageEmbed.Footer.class)
    interface FooterJson {

        String text();

        @JsonProperty("icon_url")
        Optional<String> iconUrl();

        @JsonProperty("proxy_icon_url")
        Optional<String> proxyIconUrl();
    }

    @ImmutableJson
    @JsonDeserialize(as = MessageEmbed.Field.class)
    interface FieldJson {

        String name();

        String value();

        Optional<Boolean> inline();
    }
}
