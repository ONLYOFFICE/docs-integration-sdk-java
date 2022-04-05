package base.processor.post;

import core.processor.post.OnlyofficeEditorPostProcessor;

import java.util.UUID;

public class OnlyofficeDefaultEditorPostProcessor implements OnlyofficeEditorPostProcessor {
    public String postprocessorName() {
        return UUID.randomUUID().toString();
    }
}
