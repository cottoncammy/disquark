package io.disquark.rest.json.forum;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;
import io.disquark.rest.json.message.AllowedMentions;
import io.disquark.rest.json.message.Message;
import io.disquark.rest.json.message.MessageEmbed;
import io.disquark.rest.json.message.PartialAttachment;
import io.disquark.rest.json.messagecomponent.Component;
import io.disquark.rest.request.MultipartRequest;

@ImmutableJson
interface ForumThreadMessageParamsJson extends MultipartRequest {

    Optional<String> content();

    Optional<List<MessageEmbed>> embeds();

    @JsonProperty("allowed_mentions")
    Optional<AllowedMentions> allowedMentions();

    Optional<List<Component>> components();

    @JsonProperty("sticker_ids")
    Optional<List<Snowflake>> stickerIds();

    Optional<List<PartialAttachment>> attachments();

    Optional<EnumSet<Message.Flag>> flags();
}
