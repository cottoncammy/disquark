package org.example.rest;

import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpClientResponse;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.rest.interactions.DiscordInteractionsClient;
import org.example.rest.request.AccessTokenSource;
import org.example.rest.util.Hex;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.*;

import static org.example.rest.interactions.dsl.InteractionSchema.ping;

class InteractionsBouncyCastleValidatorTest extends InteractionsTestBase {
    private static DiscordBotClient<?> botClient;
    private static byte[] privateKey;

    @BeforeAll
    static void init() {
        Security.addProvider(new BouncyCastleProvider());
        Ed25519KeyPairGenerator gen = new Ed25519KeyPairGenerator();
        gen.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        AsymmetricCipherKeyPair keyPair = gen.generateKeyPair();
        privateKey = ((Ed25519PrivateKeyParameters) keyPair.getPrivate()).getEncoded();

        botClient = DiscordBotClient.builder(Vertx.vertx(), AccessTokenSource.DUMMY)
                .interactionsClientOptions(new DiscordInteractionsClient.Options()
                        .setVerifyKey(Hex.encode(((Ed25519PublicKeyParameters) keyPair.getPublic()).getEncoded())))
                .build();

        botClient.on(ping());
    }

    @AfterAll
    static void cleanup() {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        botClient.getVertx().closeAndAwait();
    }

    @Test
    void testValidation() throws CryptoException {
        Signer signer = new Ed25519Signer();
        signer.init(true, new Ed25519PrivateKeyParameters(privateKey, 0));

        String timestamp = "foo";
        String body = buildPing();
        byte[] msg = (timestamp + body).getBytes(StandardCharsets.UTF_8);
        signer.update(msg, 0, msg.length);
        String signature = Hex.encode(signer.generateSignature());

        sendInteraction(botClient, signature, timestamp, body)
                .map(HttpClientResponse::statusCode)
                .subscribe().withSubscriber(UniAssertSubscriber.create())
                .awaitItem()
                .assertItem(200);
    }
}
