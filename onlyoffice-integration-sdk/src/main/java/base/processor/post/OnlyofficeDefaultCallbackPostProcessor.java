package base.processor.post;

import core.processor.post.OnlyofficeCallbackPostProcessor;

import java.util.UUID;

public class OnlyofficeDefaultCallbackPostProcessor implements OnlyofficeCallbackPostProcessor {
    public String postprocessorName() {
        return UUID.randomUUID().toString();
    }
}
