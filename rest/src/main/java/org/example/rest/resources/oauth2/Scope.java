package org.example.rest.resources.oauth2;

import static java.util.Objects.requireNonNull;

public enum Scope {
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
    RPC("rpc"),
    RPC_ACTIVITIES_READ("rpc.activities.read"),
    RPC_NOTIFICATIONS_READ("rpc.notifications.read"),
    RPC_VOICE_READ("rpc.voice.read"),
    RPC_VOICE_WRITE("rpc.voice.write"),
    VOICE("voice"),
    WEBHOOK_INCOMING("webhook.incoming");

    private final String value;

    public static Scope create(String value) {
        switch (requireNonNull(value)) {
            case "activities.read":
                return ACTIVITIES_READ;
            case "activities.write":
                return ACTIVITIES_WRITE;
            case "applications.builds.read":
                return APPLICATIONS_BUILDS_READ;
            case "applications.builds.upload":
                return APPLICATIONS_BUILDS_UPLOAD;
            case "applications.commands":
                return APPLICATIONS_COMMANDS;
            case "applications.commands.update":
                return APPLICATIONS_COMMANDS_UPDATE;
            case "applications.commands.permissions.update":
                return APPLICATIONS_COMMANDS_PERMISSIONS_UPDATE;
            case "applications.entitlements":
                return APPLICATIONS_ENTITLEMENTS;
            case "applications.store.update":
                return APPLICATIONS_STORE_UPDATE;
            case "bot":
                return BOT;
            case "connections":
                return CONNECTIONS;
            case "dm_channels.read":
                return DM_CHANNELS_READ;
            case "email":
                return EMAIL;
            case "gdm.join":
                return GDM_JOIN;
            case "guilds":
                return GUILDS;
            case "guilds.join":
                return GUILDS_JOIN;
            case "guilds.members.read":
                return GUILDS_MEMBERS_READ;
            case "identify":
                return IDENTIFY;
            case "messages.read":
                return MESSAGES_READ;
            case "relationships.read":
                return RELATIONSHIPS_READ;
            case "rpc":
                return RPC;
            case "rpc.activities.read":
                return RPC_ACTIVITIES_READ;
            case "rpc.notifications.read":
                return RPC_NOTIFICATIONS_READ;
            case "rpc.voice.read":
                return RPC_VOICE_READ;
            case "rpc.voice.write":
                return RPC_VOICE_WRITE;
            case "voice":
                return VOICE;
            case "webhook.incoming":
                return WEBHOOK_INCOMING;
            default:
                throw new IllegalArgumentException();
        }
    }

    Scope(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
