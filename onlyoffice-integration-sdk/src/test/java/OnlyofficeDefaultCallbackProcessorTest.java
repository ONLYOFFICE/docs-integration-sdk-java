import base.processor.OnlyofficeDefaultCallbackProcessor;
import base.registry.OnlyofficeDefaultCallbackRegistry;
import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import core.runner.callback.CallbackRequest;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void addHandler() {
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
    }

    @AfterEach
    public void removeHandler() {
        this.registry.removeByCode(3);
    }

    @Test
    public void processNullCallbackRequestParameterIgnoreTest() {
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackProcessor.process(null));
    }

    @Test
    public void processNoCallbackParametersIgnoreTest() {
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackProcessor.process(
                CallbackRequest
                        .builder()
                        .callback(null)
                        .build()
        ));
    }

    @Test
    public void processNoJwtParametersTest() {
        assertDoesNotThrow(() -> this.callbackProcessor.process(
                CallbackRequest
                        .builder()
                        .callback(
                                Callback
                                        .builder()
                                        .status(3)
                                        .build()
                        )
                        .build()
        ));
    }

    @Test
    public void processValidCallbackSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .secret("secret")
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.process(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
        assertEquals("new", callback.getKey());
    }

    @Test
    public void processNoJwtSecretIgnoreValidationTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.process(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
        assertEquals("1234", callback.getKey());
    }

    @Test
    public void processCallbackInvalidCallbackSecretTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .secret("invalid")
                .build();
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackProcessor.process(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
    }
}
