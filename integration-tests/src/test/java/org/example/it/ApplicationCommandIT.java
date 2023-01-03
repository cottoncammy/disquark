package org.example.it;

import org.example.it.extension.SomeExtension2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SomeExtension2.class)
class ApplicationCommandIT extends AuthenticatedDiscordClientIT {
}
