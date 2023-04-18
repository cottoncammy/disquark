package io.disquark.rest.json.message;

import java.util.Optional;

import io.disquark.immutables.ImmutableJson;
import io.disquark.rest.json.Snowflake;

@ImmutableJson
interface PartialAttachmentJson {

    Snowflake id();

    Optional<String> filename();

    Optional<String> description();
}
