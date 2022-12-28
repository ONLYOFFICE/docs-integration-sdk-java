package unit;

import base.processor.preprocessor.OnlyofficeDefaultEditorPreProcessor;
import core.model.config.Config;
import core.processor.OnlyofficeEditorPreProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class OnlyofficeEditorPreProcessorTest {
    private final OnlyofficeEditorPreProcessor configOnlyofficePreProcessor = new OnlyofficeDefaultEditorPreProcessor();

    @Test
    public void processNoJwtConfigTest() {
        Config config = Config
                .builder()
                .build();
        this.configOnlyofficePreProcessor.processBefore(config);
        assertNull(config.getToken());
    }
}
