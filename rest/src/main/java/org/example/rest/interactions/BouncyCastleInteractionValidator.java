package org.example.rest.interactions;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.example.rest.util.Hex;

class BouncyCastleInteractionValidator extends InteractionValidator {

    BouncyCastleInteractionValidator(String verifyKey) {
        super(verifyKey);
    }

    @Override
    public boolean validate(String timestamp, String body, String signature) {
        byte[] message = (timestamp + body).getBytes(StandardCharsets.UTF_8);
        return Ed25519.verify(Hex.decode(signature), 0, Hex.decode(verifyKey), 0, message, 0, message.length);
    }
}
