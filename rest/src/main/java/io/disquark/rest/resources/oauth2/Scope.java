package io.disquark.rest.resources.oauth2;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Scope {
    @JsonEnumDefaultValue
    UNKNOWN("unknown"),
    ACTIVITIES_READ("activities.read"),
    ACTIVITIES_WRITE("activities.write"),
    APPLICATIONS_BUILDS_READ("applications.builds.read"),
    APPLICATIONS_BUILDS_UPLOAD("applications.builds.upload"),
    APPLICATIONS_COMMANDS("applications.commands"),
    APPLICATIONS_COMMANDS_UPDATE("applications.commands.update"),
    APPLICATIONS_COMMANDS_PERMISSIONS_UPDATE("applications.commands.permissions.update"),
    APPLICATIONS_ENTITLEMENTS("applications.entitlements"),
    APPLICATIONS_STORE_UPDATE("applications.store.update"),
    BOT("bot"),
    CONNECTIONS("connections"),
    DM_CHANNELS_READ("dm_channels.read"),
    EMAIL("email"),
    GDM_JOIN("gdm.join"),
    GUILDS("guilds"),
    GUILDS_JOIN("guilds.join"),
    GUILDS_MEMBERS_READ("guilds.members.read"),
    IDENTIFY("identify"),
    MESSAGES_READ("messages.read"),
    RELATIONSHIPS_READ("relationships.read"),
    ROLE_CONNECTIONS_WRITE("role_connections.write"),
    RPC("rpc"),
    RPC_ACTIVITIES_WRITE("rpc.activities.write"),
    RPC_NOTIFICATIONS_READ("rpc.notifications.read"),
    RPC_VOICE_READ("rpc.voice.read"),
    RPC_VOICE_WRITE("rpc.voice.write"),
    VOICE("voice"),
    WEBHOOK_INCOMING("webhook.incoming");

    private final String value;

    Scope(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
