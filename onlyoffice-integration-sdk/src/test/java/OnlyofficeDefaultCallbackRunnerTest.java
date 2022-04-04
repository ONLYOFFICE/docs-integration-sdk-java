import base.processor.OnlyofficeDefaultCallbackProcessor;
import base.processor.pre.OnlyofficeDefaultCallbackPreProcessor;
import base.runner.callback.OnlyofficeDefaultCallbackRunner;
import core.model.callback.Callback;
import core.processor.OnlyofficePreProcessor;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import core.registry.OnlyofficeDefaultCallbackRegistry;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRegistryHandlerRuntimeException;
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
public class OnlyofficeDefaultCallbackRunnerTest {
    private final OnlyofficeCallbackRegistry callbackRegistry = new OnlyofficeDefaultCallbackRegistry();
    private final OnlyofficeJwtSecurityManager jwtManager = new OnlyofficeJwtSecurityManager();
    private final OnlyofficeDefaultCallbackProcessor callbackProcessor = new OnlyofficeDefaultCallbackProcessor(callbackRegistry, jwtManager);
    private final OnlyofficePreProcessor<Callback> callbackOnlyofficePreProcessor = new OnlyofficeDefaultCallbackPreProcessor(jwtManager);
    private final OnlyofficeDefaultCallbackRunner callbackRunner = new OnlyofficeDefaultCallbackRunner(
            callbackProcessor,
            List.of(callbackOnlyofficePreProcessor),
            new ArrayList<>()
    );
    OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler(){
        @Override
        public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {

        }

        @Override
        public Integer getCode() {
            return 2;
        }
    };

    @BeforeEach
    public void registerHandler() {
        this.callbackRegistry.register(callbackHandler);
    }

    @AfterEach
    public void removeHandler() {
        this.callbackRegistry.removeByCode(2);
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
        assertDoesNotThrow(() -> this.callbackRunner.run(callback));
        assertEquals(0, callback.getStatus());
    }

    @Test
    public void runFullValidNoValidationTest() {
        Callback callback = Callback
                .builder()
                .status(2)
                .build();
        assertDoesNotThrow(() -> this.callbackRunner.run(callback));
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
        assertDoesNotThrow(() -> this.callbackRunner.run(callback));
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
        assertDoesNotThrow(() -> this.callbackRunner.run(callback));
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
        assertThrows(OnlyofficeProcessRuntimeException.class, () -> this.callbackRunner.run(callback));
        assertEquals(2, callback.getStatus());
    }
}
