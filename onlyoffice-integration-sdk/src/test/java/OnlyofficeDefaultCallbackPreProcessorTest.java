import base.processor.pre.OnlyofficeDefaultCallbackPreProcessor;
import com.google.common.collect.ImmutableMap;
import core.model.OnlyofficeModelMutator;
import core.model.callback.Callback;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.runner.callback.CallbackRequest;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessBeforeRuntimeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeDefaultCallbackPreProcessorTest {
    private final OnlyofficeJwtSecurity jwtSecurity = new OnlyofficeJwtSecurityManager();
    private final OnlyofficeCallbackPreProcessor callbackOnlyofficePreProcessor = new OnlyofficeDefaultCallbackPreProcessor();

    @Test
    public void processNullCallbackRequestParameterIgnoreTest() {
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(null));
    }

    @Test
    public void processNoCallbackParameterIgnoreTest() {
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(
                CallbackRequest
                        .builder()
                        .callback(null)
                        .build()
        ));
    }

    @Test
    public void processNoPreProcessorParametersIgnoreTest() {
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(
                CallbackRequest
                        .builder()
                        .callback(
                                Callback
                                        .builder()
                                        .key("1234")
                                        .status(2)
                                        .build()
                        )
                        .build()
        ));
    }

    @Test
    public void processNullProcessorParametersIgnoreTest() {
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(
                CallbackRequest
                        .builder()
                        .callback(
                                Callback
                                        .builder()
                                        .key("1234")
                                        .status(2)
                                        .build()
                        )
                        .preProcessors(null)
                        .build()
        ));
    }

    @Test
    public void processNullMapPreProcessorParametersIgnoreTest() {
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(
                CallbackRequest
                        .builder()
                        .callback(
                                Callback
                                        .builder()
                                        .key("1234")
                                        .status(2)
                                        .build()
                        )
                        .preProcessors(new LinkedHashMap<>(){{
                            put("testing", null);
                        }})
                        .build()
        ));
    }

    @Test
    public void processEmptyMapPreProcessorParametersIgnoreTest() {
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(
                CallbackRequest
                        .builder()
                        .callback(
                                Callback
                                        .builder()
                                        .key("1234")
                                        .status(2)
                                        .build()
                        )
                        .preProcessors(new LinkedHashMap<>(){{
                            put("onlyoffice.preprocessor.default.callback", ImmutableMap.of());
                        }})
                        .build()
        ));
    }

    @Test
    public void processMalformedTokenTest() {
        String token = "aasdasc.asvasvas.arqwrtqwtrqw";
        assertThrows(OnlyofficeProcessBeforeRuntimeException.class, () -> this.callbackOnlyofficePreProcessor.processBefore(
                CallbackRequest
                        .builder()
                        .callback(
                                Callback
                                        .builder()
                                        .key("1234")
                                        .status(2)
                                        .token(token)
                                        .build()
                        )
                        .preProcessors(new LinkedHashMap<>(){{
                            put("onlyoffice.preprocessor.default.callback", ImmutableMap.of(
                                    "key", "secret",
                                    "token", token
                            ));
                        }})
                        .build()
        ));
    }

    @Test
    public void processValidWithAutoFillerTest() {
        class T implements OnlyofficeModelMutator<Callback> {
            private int status;

            public T() {
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public void mutate(Callback model) {
                model.setStatus(status);
            }
        }

        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtSecurity.sign(Map.of("status", 3), "secret", date).get();
        Callback callback = Callback
                .builder()
                .key("1234")
                .status(2)
                .build();
        assertDoesNotThrow(() -> this.callbackOnlyofficePreProcessor.processBefore(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .preProcessors(new LinkedHashMap<>(){{
                            put("onlyoffice.preprocessor.default.callback", ImmutableMap.of(
                                    "key", "secret",
                                    "token", token,
                                    "mutator", new T()
                            ));
                        }})
                        .build()
        ));
        assertEquals(3, callback.getStatus());
    }
}
