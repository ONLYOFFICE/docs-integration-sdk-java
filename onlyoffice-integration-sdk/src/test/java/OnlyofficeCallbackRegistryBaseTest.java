import core.callback.OnlyofficeCallbackHandler;
import core.callback.OnlyofficeCallbackRegistry;
import core.callback.OnlyofficeCallbackRegistryBase;
import core.model.callback.Callback;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OnlyofficeCallbackRegistryBaseTest {
    private final OnlyofficeCallbackRegistry registry = new OnlyofficeCallbackRegistryBase();
    @Test
    public void registerCallbackTest() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) {
            }

            @Override
            public int getStatus() {
                return 0;
            }
        };

        this.registry.registerCallbacks(callbackHandler);
        assertEquals(1, this.registry.registeredCallbacks());
    }

    @Test
    public void registerCallbackInvalidRegistrationTest() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) {
            }

            @Override
            public int getStatus() {
                return 0;
            }
        };

        this.registry.registerCallbacks(callbackHandler);
        this.registry.registerCallbacks(callbackHandler);
        assertEquals(1, this.registry.registeredCallbacks());
    }

    @Test
    public void registerCallbackUnregisterTest() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) {
            }

            @Override
            public int getStatus() {
                return 0;
            }
        };

        this.registry.registerCallbacks(callbackHandler);
        this.registry.removeCallbackByStatusCode(callbackHandler.getStatus());
        assertEquals(0, this.registry.registeredCallbacks());
    }
}
