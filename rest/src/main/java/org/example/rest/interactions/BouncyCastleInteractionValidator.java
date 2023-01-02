package org.example.rest.interactions;

import io.vertx.mutiny.core.buffer.Buffer;
import org.bouncycastle.math.ec.rfc8032.Ed25519;

class BouncyCastleInteractionValidator extends InteractionValidator {

    BouncyCastleInteractionValidator(String verifyKey) {
        super(verifyKey);
    }

    @Override
    public boolean validate(Buffer timestamp, Buffer body, Buffer signature) {
        byte[] message = timestamp.appendBuffer(body).getBytes();
        return Ed25519.verify(signature.getBytes(), 0, verifyKey.getBytes(), 0, message, 0, message.length);
    }
}
