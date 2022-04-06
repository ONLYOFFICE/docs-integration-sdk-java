import com.google.common.collect.ImmutableMap;
import core.model.config.Config;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.pre.OnlyofficeEditorPreProcessor;
import core.runner.OnlyofficeEditorRunner;
import core.runner.editor.ConfigRequest;
import core.runner.editor.OnlyofficeCustomizableEditorRunner;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeProcessRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeCustomizableEditorRunnerTest {
    private final OnlyofficeEditorProcessor onlyofficeDefaultEditorProcessor = new OnlyofficeEditorProcessor() {
        @Override
        public void process(ConfigRequest model) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {

        }
    };
    private final OnlyofficeEditorPreProcessor firstPreProcessor = new OnlyofficeEditorPreProcessor() {
        @Override
        public void processBefore(ConfigRequest model) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            if (!model.getConfig().getCustom().containsKey("counter")) {
                model.getConfig().getCustom().put("counter", 1);
            }
            if (model.getConfig() != null && model.getConfig().getSecret() != null && model.getConfig().getSecret().equals("second")) {
                model.getConfig().getCustom().put("counter", ((Integer) model.getConfig().getCustom().get("counter")) + 1);
            }
            model.getConfig().setSecret("first");
            model.addPreProcessor("preprocessor.test.second", ImmutableMap.of());
        }

        @Override
        public String preprocessorName() {
            return "preprocessor.test.first";
        }
    };
    private final OnlyofficeEditorPreProcessor secondPreProcessor = new OnlyofficeEditorPreProcessor() {
        @Override
        public void processBefore(ConfigRequest model) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {
            model.getConfig().setSecret("second");
            model.addPreProcessor("preprocessor.test.first", ImmutableMap.of());
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
                        .preProcessors(new LinkedHashMap<>(){{
                            put("preprocessor.test.first", ImmutableMap.of());
                        }})
                        .build()
        ));
        assertEquals(3, config.getCustom().get("counter"));
    }
}
