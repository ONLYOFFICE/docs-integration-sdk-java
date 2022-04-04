import base.processor.OnlyofficeDefaultCallbackProcessor;
import core.model.callback.Callback;
import core.registry.OnlyofficeDefaultCallbackRegistry;
import core.registry.OnlyofficeCallbackRegistry;
import core.registry.OnlyofficeCallbackHandler;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Replace with mocks
public class OnlyofficeDefaultCallbackProcessorTest {
    private final OnlyofficeCallbackRegistry registry = new OnlyofficeDefaultCallbackRegistry();
    private final OnlyofficeJwtSecurityManager jwtManager = new OnlyofficeJwtSecurityManager();
    private final OnlyofficeDefaultCallbackProcessor callbackProcessor = new OnlyofficeDefaultCallbackProcessor(registry, jwtManager);

    @Test
    public void handleCallbackWithoutJwtChecksTest() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {
                assertTrue(true);
            }

            @Override
            public Integer getCode() {
                return 3;
            }
        };
        this.registry.register(callbackHandler);
        Callback callback = Callback
                .builder()
                .status(3)
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.process(callback));
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
        assertDoesNotThrow(() -> this.callbackProcessor.process(callback));
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
        assertDoesNotThrow(() -> this.callbackProcessor.process(callback));
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
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackProcessor.process(callback));
    }
}
