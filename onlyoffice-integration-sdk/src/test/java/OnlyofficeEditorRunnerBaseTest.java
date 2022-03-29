import core.model.OnlyofficeModelAutoFiller;
import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.processor.OnlyofficeEditorProcessor;
import core.processor.OnlyofficeEditorProcessorBase;
import core.processor.OnlyofficePostProcessor;
import core.processor.OnlyofficePreProcessor;
import core.processor.configuration.OnlyofficeProcessorCustomMapConfiguration;
import core.processor.configuration.OnlyofficeProcessorCustomMapConfigurationBase;
import core.processor.implementation.OnlyofficeEditorPostProcessorBase;
import core.processor.implementation.OnlyofficeEditorPreProcessorBase;
import core.runner.OnlyofficeEditorRunner;
import core.runner.OnlyofficeEditorRunnerBase;
import core.security.OnlyofficeJwtManager;
import core.security.OnlyofficeJwtManagerBase;
import core.util.OnlyofficeConfigUtil;
import core.util.OnlyofficeConfigUtilBase;
import core.util.OnlyofficeFileUtil;
import core.util.OnlyofficeFileUtilBase;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OnlyofficeEditorRunnerBaseTest {
    private final OnlyofficeJwtManager jwtSecurity = new OnlyofficeJwtManagerBase();
    private final OnlyofficeFileUtil onlyofficeFileUtil = new OnlyofficeFileUtilBase();
    private final OnlyofficeConfigUtil configUtil = new OnlyofficeConfigUtilBase(onlyofficeFileUtil);
    private final OnlyofficeProcessorCustomMapConfiguration configuration = new OnlyofficeProcessorCustomMapConfigurationBase();
    private final OnlyofficeEditorProcessor onlyofficeEditorProcessor = new OnlyofficeEditorProcessorBase(configUtil);
    private final OnlyofficePreProcessor<Config> configOnlyofficePreProcessor = new OnlyofficeEditorPreProcessorBase(configuration, jwtSecurity);
    private final OnlyofficePostProcessor<Config> configOnlyofficePostProcessor = new OnlyofficeEditorPostProcessorBase(configuration, jwtSecurity);
    private final OnlyofficeEditorRunner onlyofficeEditorRunner = new OnlyofficeEditorRunnerBase(
            onlyofficeEditorProcessor,
            List.of(configOnlyofficePreProcessor),
            List.of(configOnlyofficePostProcessor)
    );

    @Test
    @SneakyThrows
    public void runFullValidTest() {
        class AF implements OnlyofficeModelAutoFiller<Config> {
            @Getter
            private String docUrl;

            public AF(String docUrl) {
                this.docUrl = docUrl;
            }

            public void fillModel(Config model) {
                model.getDocument().setUrl(docUrl);
            }
        }

        AF af = new AF("http://example.com");
        Date date = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        String token = this.jwtSecurity.sign(af, "secret", date).get();

        Document doc = Document
                .builder()
                .title("test.docx")
                .url("https://example.co")
                .key("asdd")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(doc)
                .editorConfig(editor)
                .custom(Map.of(
                        configuration.getBeforeMapKey(), Map.of(
                                configuration.getSecretKey(), "secret",
                                configuration.getTokenKey(), token,
                                configuration.getAutoFillerKey(), af
                        ),
                        configuration.getAfterMapKey(), Map.of(
                                configuration.getSecretKey(), "secret"
                        )
                ))
                .build();
        Config processedConfig = this.onlyofficeEditorRunner.run(config);
        assertNotNull(processedConfig);
        assertNotNull(processedConfig.getToken());
        assertEquals("http://example.com", processedConfig.getDocument().getUrl());
    }

    @Test
    @SneakyThrows
    public void runNoSecretValidTest() {
        Document doc = Document
                .builder()
                .title("test.docx")
                .url("https://example.com")
                .key("asdd")
                .build();
        Editor editor = Editor
                .builder()
                .callbackUrl("https://example.com")
                .build();
        Config config = Config
                .builder()
                .document(doc)
                .editorConfig(editor)
                .build();
        Config processedConfig = this.onlyofficeEditorRunner.run(config);
        assertNotNull(processedConfig);
        assertNull(processedConfig.getToken());
    }
}
