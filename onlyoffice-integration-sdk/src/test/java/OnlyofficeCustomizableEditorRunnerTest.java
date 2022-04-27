import com.google.common.collect.ImmutableMap;
import core.model.config.Config;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.preprocessor.OnlyofficeEditorPreProcessor;
import core.runner.OnlyofficeEditorRunner;
import core.runner.implementation.ConfigRequest;
import core.runner.implementation.OnlyofficeCustomizableEditorRunner;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnlyofficeCustomizableEditorRunnerTest {
    private final OnlyofficeEditorProcessor onlyofficeDefaultEditorProcessor = model -> {};

    private final OnlyofficeEditorPreProcessor<Object> firstPreProcessor = new OnlyofficeEditorPreProcessor<>() {
        public void changeProcessors(ConfigRequest request) {
            request.addPreProcessor("preprocessor.test.second", ImmutableMap.of());
        }

        public Object validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema) {
            return new Object();
        }

        public void processBefore(Config config, Object validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            if (!config.getCustom().containsKey("counter")) {
                config.getCustom().put("counter", 1);
            }
            if (config != null && config.getSecret() != null && config.getSecret().equals("second")) {
                config.getCustom().put("counter", ((Integer) config.getCustom().get("counter")) + 1);
            }
            config.setSecret("first");
        }

        public String preprocessorName() {
            return "preprocessor.test.first";
        }
    };

    private final OnlyofficeEditorPreProcessor<Object> secondPreProcessor = new OnlyofficeEditorPreProcessor<Object>() {
        public void changeProcessors(ConfigRequest request) {
            request.addPreProcessor("preprocessor.test.first", ImmutableMap.of());
        }

        public Object validateSchema(Map<String, Object> customData, ImmutableMap<String, Object> schema) {
            return new Object();
        }

        public void processBefore(Config config, Object validSchema) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            config.setSecret("second");
        }

        @Override
        public String preprocessorName() {
            return "preprocessor.test.second";
        }
    };
    
    private final OnlyofficeEditorRunner editorRunner = new OnlyofficeCustomizableEditorRunner(
            onlyofficeDefaultEditorProcessor,
            Map.of(
                    firstPreProcessor.preprocessorName(), firstPreProcessor,
                    secondPreProcessor.preprocessorName(), secondPreProcessor
            ),
            Map.of()
    );

    @Test
    public void runFullValidWithCounterTest() {
        Config config = Config.builder().build();
        assertThrows(OnlyofficeRunnerRuntimeException.class, () -> this.editorRunner.run(
                ConfigRequest
                        .builder()
                        .config(config)
                        .build()
                        .addPreProcessor("preprocessor.test.first", ImmutableMap.of())
        ));
        assertEquals(3, config.getCustom().get("counter"));
    }
}
