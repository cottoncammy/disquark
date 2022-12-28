package org.example.rest.interactions;

import org.bouncycastle.math.ec.rfc8032.Ed25519;

public class BouncyCastleInteractionValidator extends InteractionValidator {

    BouncyCastleInteractionValidator(String verifyKey) {
        super(verifyKey);
    }

    @Override
    public boolean validate(String timestamp, String body, String signature) {
        return Ed25519.verify();
    }
}
