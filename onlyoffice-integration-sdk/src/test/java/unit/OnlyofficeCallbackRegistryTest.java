package unit;

import base.registry.OnlyofficeDefaultCallbackRegistry;
import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeCallbackRegistryTest {
    private final OnlyofficeCallbackRegistry callbackRegistry = new OnlyofficeDefaultCallbackRegistry();

    @BeforeEach
    public void cleanup() {
        callbackRegistry.removeByCode(1);
    }

    @Test
    public void processRegisterNullHandler() {
        assertDoesNotThrow(() -> callbackRegistry.register(null));
    }

    @Test
    public void processRegisterHandler() {
        assertDoesNotThrow(() -> callbackRegistry.register(new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback model) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 1;
            }
        }));
        assertEquals(1, callbackRegistry.registered());
    }

    @Test
    public void processRegisterMultipleHandlers() {
        assertDoesNotThrow(() -> callbackRegistry.register(new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback model) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 1;
            }
        }));
        assertDoesNotThrow(() -> callbackRegistry.register(new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback model) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 1;
            }
        }));
        assertEquals(1, callbackRegistry.registered());
    }

    @Test
    public void processHandleCallbackRequest() {
        callbackRegistry.register(new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback model) throws OnlyofficeRegistryHandlerRuntimeException {
                throw new OnlyofficeRegistryHandlerRuntimeException("mock");
            }

            @Override
            public Integer getCode() {
                return 1;
            }
        });
        assertThrows(OnlyofficeRegistryHandlerRuntimeException.class, () -> callbackRegistry.run(Callback.builder().status(1).build()));
    }
}
