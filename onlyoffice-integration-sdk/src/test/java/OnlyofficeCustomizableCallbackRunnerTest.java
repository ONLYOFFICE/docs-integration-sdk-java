import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.preprocessor.OnlyofficeCallbackPreProcessor;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.implementation.CallbackRequest;
import core.runner.implementation.OnlyofficeCustomizableCallbackRunner;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnlyofficeCustomizableCallbackRunnerTest {
    private final OnlyofficeCallbackProcessor callbackProcessor = new OnlyofficeCallbackProcessor() {
        public void process(Callback model) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {}
    };

    private final OnlyofficeCallbackPreProcessor firstPreProcessor = new OnlyofficeCallbackPreProcessor<Object>() {
        public void changeProcessors(CallbackRequest request) {
           request.addPreProcessor("preprocessor.test.second", ImmutableMap.of());
        }

        public Object validateSchema(Map<String, Object> custoData, ImmutableMap<String, Object> schema) {
            return new Object();
        }

        public void processBefore(Callback callback, Object validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            if (!callback.getCustom().containsKey("counter")) {
                callback.getCustom().put("counter", 1);
            }
            if (callback != null && callback.getSecret() != null && callback.getSecret().equals("second")) {
                callback.getCustom().put("counter", ((Integer) callback.getCustom().get("counter")) + 1);
            }
            callback.setSecret("first");
        }

        public String preprocessorName() {
            return "preprocessor.test.first";
        }
    };

    private final OnlyofficeCallbackPreProcessor<Object> secondPreProcessor = new OnlyofficeCallbackPreProcessor<Object>() {
        public void changeProcessors(CallbackRequest request) {
            request.addPreProcessor("preprocessor.test.third", ImmutableMap.of());
        }

        public Object validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema) {
            return new Object();
        }

        public void processBefore(Callback callback, Object validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            callback.setSecret("second");
        }

        public String preprocessorName() {
            return "preprocessor.test.second";
        }
    };

    private final OnlyofficeCallbackPreProcessor<Object> thirdPreProcessor = new OnlyofficeCallbackPreProcessor<Object>() {
        public void changeProcessors(CallbackRequest request) {
            request.addPreProcessor("preprocessor.test.first", ImmutableMap.of());
        }

        public Object validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema) {
            return new Object();
        }

        @Override
        public void processBefore(Callback callback, Object validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            callback.getCustom().put("counter", (Integer) callback.getCustom().get("counter") + 1);
        }

        @Override
        public String preprocessorName() {
            return "preprocessor.test.third";
        }
    };

    private final OnlyofficeCallbackRunner callbackRunner = new OnlyofficeCustomizableCallbackRunner(
            callbackProcessor,
            Map.of(
                    firstPreProcessor.preprocessorName(), firstPreProcessor,
                    secondPreProcessor.preprocessorName(), secondPreProcessor,
                    thirdPreProcessor.preprocessorName(), thirdPreProcessor
            ),
            Map.of()
    );

    @Test
    public void runFullValidWithCounterTest() {
        Callback callback = Callback.builder().build();
        assertThrows(OnlyofficeRunnerRuntimeException.class, () -> this.callbackRunner.run(
                CallbackRequest
                        .builder()
                        .callback(callback)
                        .build()
                        .addPreProcessor("preprocessor.test.first", ImmutableMap.of())
        ));
        assertEquals(6, callback.getCustom().get("counter"));
    }
}
