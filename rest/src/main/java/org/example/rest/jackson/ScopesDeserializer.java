package org.example.rest.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.rest.resources.oauth2.Scope;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.example.rest.resources.oauth2.Scope.*;

public class ScopesDeserializer extends JsonDeserializer<List<Scope>> {

    private Scope getScope(String value) {
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
            case "role_connections.write":
                return ROLE_CONNECTIONS_WRITE;
            case "rpc":
                return RPC;
            case "rpc.activities.write":
                return RPC_ACTIVITIES_WRITE;
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

    @Override
    public List<Scope> deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String s = p.getText();

        // TODO
        try {
            return Arrays.stream(s.split(" ")).map(this::getScope).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new InvalidFormatException(p, "Expected a space-separated list of Discord OAuth2 scopes", s, List.class);
        }
    }
}
