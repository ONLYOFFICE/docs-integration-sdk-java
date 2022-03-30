import com.auth0.jwt.exceptions.JWTVerificationException;
import core.model.OnlyofficeModelAutoFiller;
import core.model.callback.Callback;
import core.processor.OnlyofficePreProcessor;
import core.processor.schema.OnlyofficeDefaultPrePostProcessorMapSchema;
import core.processor.implementation.OnlyofficeCallbackDefaultPreProcessor;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// TODO: Replace with mocks
public class OnlyofficeCallbackDefaultPreProcessorTest {
    private final OnlyofficeDefaultPrePostProcessorMapSchema configuration = new OnlyofficeDefaultPrePostProcessorMapSchema();
    private final OnlyofficeJwtManager jwtManager = new OnlyofficeJwtManagerBase();
    private final OnlyofficePreProcessor<Callback> callbackOnlyofficePreProcessor = new OnlyofficeCallbackDefaultPreProcessor(configuration, jwtManager);

    @Test
    public void processNoCustomParametersIgnoreTest() {
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
    }

    @Test
    public void processInvalidBeforeMapTypeIgnoreTest() {
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), new ArrayList<>()
                ))
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
    }

    @Test
    public void processMalformedTokenThrowsTest() {
        String token = "aasdasc.asvasvas.arqwrtqwtrqw";
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .token(token)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                configuration.getTokenKey(), token
                        )
                ))
                .build();
        assertThrows(JWTVerificationException.class, () -> this.callbackOnlyofficePreProcessor.processBefore(callback));
    }

    @Test
    public void processValidWithAutoFillerTest() {
        class T implements OnlyofficeModelAutoFiller<Callback> {
            private int status;

            public T() {
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void fillModel(Callback model) {
                model.setStatus(this.status);
            }
        }

        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtManager.sign(Map.of("status", 3), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                configuration.getTokenKey(), token,
                                configuration.getAutoFillerKey(), new T()
                        )
                ))
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(callback));
        assertEquals(3, callback.getStatus());
    }
}
