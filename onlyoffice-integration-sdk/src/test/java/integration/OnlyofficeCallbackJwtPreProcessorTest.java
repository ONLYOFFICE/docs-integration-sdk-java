package integration;

import base.processor.preprocessor.OnlyofficeCallbackJwtPreProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackPreProcessor;
import core.security.OnlyofficeJwtSecurity;
import core.security.OnlyofficeJwtSecurityManager;
import exception.OnlyofficeProcessBeforeRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeCallbackJwtPreProcessorTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OnlyofficeJwtSecurity jwtSecurity = new OnlyofficeJwtSecurityManager(objectMapper);
    private final OnlyofficeCallbackPreProcessor callbackOnlyofficePreProcessor = new OnlyofficeCallbackJwtPreProcessor(jwtSecurity);

    @Test
    public void processNullCallbackTest() {
        callbackOnlyofficePreProcessor.setJwtSecret("secret");
        assertThrows(OnlyofficeProcessBeforeRuntimeException.class, () -> this.callbackOnlyofficePreProcessor.processBefore(null));
    }

    @Test
    public void processInvalidCallbackTokenTest() {
        Callback cn = Callback
                .builder()
                .key("asdc")
                .build();
        this.callbackOnlyofficePreProcessor.processBefore(cn);
        assertNull(cn.getToken());
    }

    @Test
    public void processValidCallbackTokenTest() {
        Callback cn = Callback
                .builder()
                .token("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNb2NrIiwiaWF0IjoxNjcyMDYzMjk1LCJleHAiOjgwMTQ5NDY0OTUsImF1ZCI6Im1vY2siLCJzdWIiOiJtb2NrIiwia2V5IjoiYXNkYyJ9.O0r8q01w9o5v-6LTBeHSLcUniVUChjuRvYadgD6TjTg")
                .build();
        this.callbackOnlyofficePreProcessor.processBefore(cn);
        assertEquals("asdc", cn.getKey());
    }
}
