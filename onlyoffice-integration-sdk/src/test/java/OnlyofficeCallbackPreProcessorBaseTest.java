import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.callback.Callback;
import core.processor.OnlyofficePreProcessor;
import core.processor.configuration.OnlyofficeDefaultProcessorCustomMapConfiguration;
import core.processor.implementation.OnlyofficeCallbackPreProcessorBase;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeCallbackPreProcessorBaseTest {
    private final OnlyofficeDefaultProcessorCustomMapConfiguration configuration = new OnlyofficeDefaultProcessorCustomMapConfiguration();
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final OnlyofficePreProcessor<Callback> callbackOnlyofficePreProcessor = new OnlyofficeCallbackPreProcessorBase(configuration, jwtManager);

    @Test
    public void processValidStringSignatureCallbackTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret"
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
        assertEquals("new", callback.getKey());
    }

    @Test
    public void processValidStringSignatureMapTokenTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
        assertEquals("new", callback.getKey());
    }

    @Test
    public void processInvalidStringSignatureCallbackTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
        assertNotNull(callback.getCustom());
        assertNotEquals("new", callback.getKey());
    }

    @Test
    public void processInvalidStringSignatureMapTokenTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("key", "new"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
        assertNotNull(callback.getCustom());
        assertNotEquals("new", callback.getKey());
    }

    @Test
    public void processValidIgnoreCallbackSignatureTest() {
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
        assertNotNull(callback.getCustom());
    }

    @Test
    public void processInvalidStringSignatureTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("payload", "test"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "invalid",
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertThrows(JWTVerificationException.class, () -> this.callbackOnlyofficePreProcessor.processBefore(callback));
    }

    @Test
    public void processInvalidCustomTypeIgnoreJwtTest() {
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("payload", "test"), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), "asdsd"
                ))
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
    }
}
