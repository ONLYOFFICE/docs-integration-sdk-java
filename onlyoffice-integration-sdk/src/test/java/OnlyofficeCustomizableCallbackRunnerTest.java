import com.google.common.collect.ImmutableMap;
import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackProcessor;
import core.processor.pre.OnlyofficeCallbackPreProcessor;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.callback.CallbackRequest;
import core.runner.callback.OnlyofficeCustomizableCallbackRunner;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnlyofficeCustomizableCallbackRunnerTest {
    private final OnlyofficeCallbackProcessor callbackProcessor = new OnlyofficeCallbackProcessor() {
        @Override
        public void process(CallbackRequest model) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {

        }
    };
    private final OnlyofficeCallbackPreProcessor firstPreProcessor = new OnlyofficeCallbackPreProcessor() {
        @Override
        public void processBefore(CallbackRequest model) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            if (!model.getCallback().getCustom().containsKey("counter")) {
                model.getCallback().getCustom().put("counter", 1);
            }
            if (model.getCallback() != null && model.getCallback().getSecret() != null && model.getCallback().getSecret().equals("second")) {
                model.getCallback().getCustom().put("counter", ((Integer) model.getCallback().getCustom().get("counter")) + 1);
            }
            model.getCallback().setSecret("first");
            model.addPreProcessor("preprocessor.test.second", ImmutableMap.of());
        }

        @Override
        public String preprocessorName() {
            return "preprocessor.test.first";
        }
    };

    private final OnlyofficeCallbackPreProcessor secondPreProcessor = new OnlyofficeCallbackPreProcessor() {
        @Override
        public void processBefore(CallbackRequest model) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            model.getCallback().setSecret("second");
            model.addPreProcessor("preprocessor.test.third", ImmutableMap.of());
        }

        @Override
        public String preprocessorName() {
            return "preprocessor.test.second";
        }
    };

    private final OnlyofficeCallbackPreProcessor thirdPreProcessor = new OnlyofficeCallbackPreProcessor() {
        @Override
        public void processBefore(CallbackRequest model) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            model.getCallback().getCustom().put("counter", (Integer) model.getCallback().getCustom().get("counter") + 1);
            model.addPreProcessor("preprocessor.test.first", ImmutableMap.of());
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
