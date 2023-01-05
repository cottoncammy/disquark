package org.example.rest;

import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.util.Hex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.time.Instant;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static org.example.rest.interactions.dsl.InteractionSchema.ping;

class InteractionsBouncyCastleValidatorTest extends InteractionsTestBase {
    private static DiscordBotClient<?> botClient;
    private static byte[] privateKey;

    @BeforeAll
    static void init() throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keyPair = KeyPairGenerator.getInstance("Ed25519").genKeyPair();
        privateKey = keyPair.getPrivate().getEncoded();

        botClient = DiscordBotClient.builder(Vertx.vertx(), AccessTokenSource.DUMMY)
                .interactionsClientOptions(new DiscordInteractionsClient.Options()
                        .setVerifyKey(Hex.encode(keyPair.getPublic().getEncoded())))
                .build();
    }

    @Test
    void testPing() throws CryptoException {
        botClient.on(ping());

        Signer signer = new Ed25519Signer();
        signer.init(true, new Ed25519PrivateKeyParameters(privateKey, 0));

        String timestamp = ISO_DATE_TIME.format(Instant.now());
        String body = Json.encode(buildPing());
        byte[] msg = (timestamp + body).getBytes(StandardCharsets.UTF_8);
        signer.update(msg, 0, msg.length);

        String signature = Hex.encode(signer.generateSignature());
        assertPongReceived(botClient, signature, timestamp, body);
    }
}
