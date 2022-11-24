package org.example.rest.resources.permissions;

public enum PermissionFlag {
    CREATE_INSTANT_INVITE(0),
    KICK_MEMBERS(1),
    BAN_MEMBERS(2),
    ADMINISTRATOR(3),
    MANAGE_CHANNELS(4),
    MANAGE_GUILD(5),
    ADD_REACTIONS(6),
    VIEW_AUDIT_LOG(7),
    PRIORITY_SPEAKER(8),
    STREAM(9),
    VIEW_CHANNEL(10),
    SEND_MESSAGES(11),
    SEND_TTS_MESSAGES(12),
    MANAGE_MESSAGES(13),
    EMBED_LINKS(14),
    ATTACH_FILES(15),
    READ_MESSAGE_HISTORY(16),
    MENTION_EVERYONE(17),
    USE_EXTERNAL_EMOJIS(18),
    VIEW_GUILD_INSIGHTS(19),
    CONNECT(20),
    SPEAK(21),
    MUTE_MEMBERS(22),
    DEAFEN_MEMBERS(23),
    MOVE_MEMBERS(24),
    USE_VAD(25),
    CHANGE_NICKNAME(26),
    MANAGE_NICKNAMES(27),
    MANAGE_ROLES(28),
    MANAGE_WEBHOOKS(29),
    MANAGE_EMOJIS_AND_STICKERS(30),
    USE_APPLICATION_COMMANDS(31),
    REQUEST_TO_SPEAK(32),
    MANAGE_EVENTS(33),
    MANAGE_THREADS(34),
    CREATE_PUBLIC_THREADS(35),
    CREATE_PRIVATE_THREADS(36),
    USE_EXTERNAL_STICKERS(37),
    SEND_MESSAGES_IN_THREADS(38),
    USE_EMBEDDED_ACTIVITIES(39),
    MODERATE_MEMBERS(40);

    private final int value;

    public static PermissionFlag create(int value) {
        switch (value) {
            case 0:
                return CREATE_INSTANT_INVITE;
            case 1:
                return KICK_MEMBERS;
            case 2:
                return BAN_MEMBERS;
            case 3:
                return ADMINISTRATOR;
            case 4:
                return MANAGE_CHANNELS;
            case 5:
                return MANAGE_GUILD;
            case 6:
                return ADD_REACTIONS;
            case 7:
                return VIEW_AUDIT_LOG;
            case 8:
                return PRIORITY_SPEAKER;
            case 9:
                return STREAM;
            case 10:
                return VIEW_CHANNEL;
            case 11:
                return SEND_MESSAGES;
            case 12:
                return SEND_TTS_MESSAGES;
            case 13:
                return MANAGE_MESSAGES;
            case 14:
                return EMBED_LINKS;
            case 15:
                return ATTACH_FILES;
            case 16:
                return READ_MESSAGE_HISTORY;
            case 17:
                return MENTION_EVERYONE;
            case 18:
                return USE_EXTERNAL_EMOJIS;
            case 19:
                return VIEW_GUILD_INSIGHTS;
            case 20:
                return CONNECT;
            case 21:
                return SPEAK;
            case 22:
                return MUTE_MEMBERS;
            case 23:
                return DEAFEN_MEMBERS;
            case 24:
                return MOVE_MEMBERS;
            case 25:
                return USE_VAD;
            case 26:
                return CHANGE_NICKNAME;
            case 27:
                return MANAGE_NICKNAMES;
            case 28:
                return MANAGE_ROLES;
            case 29:
                return MANAGE_WEBHOOKS;
            case 30:
                return MANAGE_EMOJIS_AND_STICKERS;
            case 31:
                return USE_APPLICATION_COMMANDS;
            case 32:
                return REQUEST_TO_SPEAK;
            case 33:
                return MANAGE_EVENTS;
            case 34:
                return MANAGE_THREADS;
            case 35:
                return CREATE_PUBLIC_THREADS;
            case 36:
                return CREATE_PRIVATE_THREADS;
            case 37:
                return USE_EXTERNAL_STICKERS;
            case 38:
                return SEND_MESSAGES_IN_THREADS;
            case 39:
                return USE_EMBEDDED_ACTIVITIES;
            case 40:
                return MODERATE_MEMBERS;
            default:
                throw new IllegalArgumentException();
        }
    }

    PermissionFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
