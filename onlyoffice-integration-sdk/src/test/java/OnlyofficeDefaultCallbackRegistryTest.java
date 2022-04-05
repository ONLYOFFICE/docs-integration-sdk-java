import core.model.callback.Callback;
import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import base.registry.OnlyofficeDefaultCallbackRegistry;
import exception.OnlyofficeRegistryHandlerRuntimeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO: Replace with mocks
public class OnlyofficeDefaultCallbackRegistryTest {
    private final OnlyofficeCallbackRegistry registry = new OnlyofficeDefaultCallbackRegistry();
    @Test
    public void registerCallbackTest() {
        OnlyofficeCallbackHandler handler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 0;
            }
        };
        this.registry.register(handler);
        assertEquals(1, this.registry.registered());
    }

    @Test
    public void registerCallbackInvalidRegistrationTest() {
        OnlyofficeCallbackHandler handler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 0;
            }
        };

        this.registry.register(handler);
        this.registry.register(handler);
        assertEquals(1, this.registry.registered());
    }

    @Test
    public void registerCallbackUnregisterTest() {
        OnlyofficeCallbackHandler handler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) throws OnlyofficeRegistryHandlerRuntimeException {

            }

            @Override
            public Integer getCode() {
                return 0;
            }
        };

        this.registry.register(handler);
        this.registry.removeByCode(handler.getCode());
        assertEquals(0, this.registry.registered());
    }

    @Test
    public void registerNullCallbackHandlerTest() {
        this.registry.register(null);
        assertEquals(0, this.registry.registered());
    }
}
