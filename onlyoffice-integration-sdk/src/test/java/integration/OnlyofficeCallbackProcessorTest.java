package integration;

import base.registry.OnlyofficeDefaultCallbackRegistry;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeCallbackProcessorTest {
    private final OnlyofficeCallbackRegistry registry = new OnlyofficeDefaultCallbackRegistry();
    private final OnlyofficeCallbackProcessor callbackProcessor = new OnlyofficeCallbackProcessor(registry);

    @BeforeEach
    public void addHandler() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {
                callback.setToken("mock");
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
    public void processNoJwtParametersTest() {
        Callback c = Callback
                .builder()
                .status(3)
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.process(c));
        assertEquals("mock", c.getToken());
    }
}
