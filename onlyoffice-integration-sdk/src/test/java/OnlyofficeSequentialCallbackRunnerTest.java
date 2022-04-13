import base.processor.OnlyofficeDefaultCallbackProcessor;
import base.processor.pre.OnlyofficeDefaultCallbackPreProcessor;
import base.registry.OnlyofficeDefaultCallbackRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.callback.Callback;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import core.runner.callback.CallbackRequest;
import core.runner.callback.OnlyofficeSequentialCallbackRunner;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Replace with mocks
public class OnlyofficeSequentialCallbackRunnerTest {
    private final OnlyofficeCallbackRegistry callbackRegistry = new OnlyofficeDefaultCallbackRegistry();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeJwtSecurityManager jwtManager = new OnlyofficeJwtSecurityManager(objectMapper);
    private final OnlyofficeDefaultCallbackProcessor callbackProcessor = new OnlyofficeDefaultCallbackProcessor(callbackRegistry, jwtManager);
    private final OnlyofficeCallbackPreProcessor callbackOnlyofficePreProcessor = new OnlyofficeDefaultCallbackPreProcessor(objectMapper);
    private final OnlyofficeSequentialCallbackRunner callbackRunner = new OnlyofficeSequentialCallbackRunner(
            callbackProcessor,
            Map.of(callbackOnlyofficePreProcessor.preprocessorName(), callbackOnlyofficePreProcessor),
            Map.of()
    );

    @BeforeEach
    public void registerHandler() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler(){
            @Override
            public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 2;
            }
        };
        this.callbackRegistry.register(callbackHandler);
    }

    @AfterEach
    public void removeHandler() {
        this.callbackRegistry.removeByCode(2);
    }

    @Test
    public void runNullCallbackRequestTest() {
        assertThrows(OnlyofficeRunnerRuntimeException.class, () -> this.callbackRunner.run(null));
    }

    @Test
    public void runEmptyCallbackRequestTest() {
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackRunner.run(
                CallbackRequest
                        .builder()
                        .build()
        ));
    }

    @Test
    public void runFullValidJwtTest() {
        Date expiresAt = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("status", 0), "secret", expiresAt).get();
        Callback callback = Callback
                .builder()
                .status(2)
                .secret("secret")
                .token(token)
                .build();
        assertDoesNotThrow(() -> this.callbackRunner.run(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
        assertEquals(0, callback.getStatus());
    }

    @Test
    public void runFullValidNoValidationTest() {
        Callback callback = Callback
                .builder()
                .status(2)
                .build();
        assertDoesNotThrow(() -> this.callbackRunner.run(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
        assertEquals(2, callback.getStatus());
        assertNotNull(callback.getCustom());
    }

    @Test
    public void runInvalidSkipJwtNoSecretTest() {
        Date expiresAt = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("status", 0), "secret", expiresAt).get();
        Callback callback = Callback
                .builder()
                .status(2)
                .token(token)
                .build();
        assertDoesNotThrow(() -> this.callbackRunner.run(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
        assertEquals(2, callback.getStatus());
        assertNotNull(callback.getCustom());
    }

    @Test
    public void runInvalidSkipJwtNoTokenTest() {
        Callback callback = Callback
                .builder()
                .status(2)
                .secret("secret")
                .build();
        assertDoesNotThrow(() -> this.callbackRunner.run(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
        assertEquals(2, callback.getStatus());
    }

    @Test
    public void runInvalidJwtValidationTest() {
        Date expiresAt = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("status", 0), "secret", expiresAt).get();
        Callback callback = Callback
                .builder()
                .status(2)
                .secret("invalid")
                .token(token)
                .build();
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackRunner.run(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
        ));
        assertEquals(2, callback.getStatus());
    }
}
