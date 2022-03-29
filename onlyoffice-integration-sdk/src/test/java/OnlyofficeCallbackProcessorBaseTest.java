import core.callback.OnlyofficeCallbackHandler;
import core.callback.OnlyofficeCallbackRegistry;
import core.callback.OnlyofficeCallbackRegistryBase;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.OnlyofficeCallbackProcessorBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnlyofficeCallbackProcessorBaseTest {
    private final OnlyofficeCallbackRegistry registry = new OnlyofficeCallbackRegistryBase();
    private final OnlyofficeCallbackProcessor callbackProcessor = new OnlyofficeCallbackProcessorBase(registry);

    @Test
    public void handleCallbackTest() {
        OnlyofficeCallbackHandler callbackHandler = new OnlyofficeCallbackHandler() {
            @Override
            public void handle(Callback callback) {
                assertTrue(true);
            }

            @Override
            public int getStatus() {
                return 3;
            }
        };
        this.registry.registerCallbacks(callbackHandler);
        Callback callback = Callback
                .builder()
                .status(3)
                .build();
        assertDoesNotThrow(() -> this.callbackProcessor.handleCallback(callback));
    }
}
