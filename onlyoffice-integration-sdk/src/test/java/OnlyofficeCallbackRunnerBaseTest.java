import com.auth0.jwt.exceptions.JWTVerificationException;
import core.callback.OnlyofficeCallbackHandler;
import core.callback.OnlyofficeCallbackRegistry;
import core.callback.OnlyofficeCallbackRegistryBase;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.OnlyofficeCallbackProcessorBase;
import core.processor.OnlyofficePreProcessor;
import core.processor.implementation.OnlyofficeCallbackDefaultPreProcessor;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.OnlyofficeCallbackRunnerBase;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
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
public class OnlyofficeCallbackRunnerBaseTest {
    private final OnlyofficeCallbackRegistry callbackRegistry = new OnlyofficeCallbackRegistryBase();
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final OnlyofficeCallbackProcessor callbackProcessor = new OnlyofficeCallbackProcessorBase(callbackRegistry, jwtManager);
    private final OnlyofficePreProcessor<Callback> callbackOnlyofficePreProcessor = new OnlyofficeCallbackDefaultPreProcessor(jwtManager);
    private final OnlyofficeCallbackRunner callbackRunner = new OnlyofficeCallbackRunnerBase(
            callbackProcessor,
            List.of(callbackOnlyofficePreProcessor),
            new ArrayList<>()
    );

    @BeforeEach
    public void registerHandler() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) {
            }

            @Override
            public int getStatus() {
                return 2;
            }
        };
        this.callbackRegistry.registerCallbacks(callbackHandler);
    }

    @AfterEach
    public void removeHandler() {
        this.callbackRegistry.removeCallbackByStatusCode(2);
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
        assertThrows(JWTVerificationException.class, () -> this.callbackRunner.run(callback));
        assertEquals(2, callback.getStatus());
    }
}
