package base.processor.post;

import core.model.config.Config;
import core.processor.OnlyofficePostProcessor;

import java.util.UUID;

public class OnlyofficeDefaultEditorPostProcessor implements OnlyofficePostProcessor<Config> {
    public String postprocessorName() {
        return UUID.randomUUID().toString();
    }
}
