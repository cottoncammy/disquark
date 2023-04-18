package io.disquark.rest.interactions;

import java.nio.charset.StandardCharsets;

import io.disquark.rest.util.Hex;

import org.bouncycastle.math.ec.rfc8032.Ed25519;

class BouncyCastleInteractionsValidator extends InteractionsValidator {

    BouncyCastleInteractionsValidator(String verifyKey) {
        super(verifyKey);
    }

    @Override
    public boolean validate(String timestamp, String body, String signature) {
        byte[] message = (timestamp + body).getBytes(StandardCharsets.UTF_8);
        return Ed25519.verify(Hex.decode(signature), 0, Hex.decode(verifyKey), 0, message, 0, message.length);
    }
}
