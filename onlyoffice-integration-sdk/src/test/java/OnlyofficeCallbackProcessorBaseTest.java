import com.auth0.jwt.exceptions.JWTVerificationException;
import core.callback.OnlyofficeCallbackHandler;
import core.callback.OnlyofficeCallbackRegistry;
import core.callback.OnlyofficeCallbackRegistryBase;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.OnlyofficeCallbackProcessorBase;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Replace with mocks
public class OnlyofficeCallbackProcessorBaseTest {
    private final OnlyofficeCallbackRegistry registry = new OnlyofficeCallbackRegistryBase();
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final OnlyofficeCallbackProcessor callbackProcessor = new OnlyofficeCallbackProcessorBase(registry, jwtManager);

    @Test
    public void handleCallbackWithoutJwtChecksTest() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) {
                assertTrue(true);
            }

            @Override
            public int getStatus() {
                return 3;
            }
        };
        this.registry.registerCallbacks(callbackHandler);
        Callback callback = Callback
                .builder()
                .status(3)
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.handleCallback(callback));
    }

    @Test
    public void handleCallbackValidCallbackSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .secret("secret")
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.handleCallback(callback));
        assertEquals("new", callback.getKey());
    }

    @Test
    public void handleCallbackIgnoreJwtValidationTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.handleCallback(callback));
        assertEquals("1234", callback.getKey());
    }

    @Test
    public void handleCallbackInvalidCallbackTokenTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .secret("invalid")
                .build();
        assertThrows(JWTVerificationException.class, () -> this.callbackProcessor.handleCallback(callback));
    }
}
