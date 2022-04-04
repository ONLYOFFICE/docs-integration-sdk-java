package base.processor.post;

import core.model.callback.Callback;
import core.processor.OnlyofficePostProcessor;

import java.util.UUID;

public class OnlyofficeDefaultCallbackPostProcessor implements OnlyofficePostProcessor<Callback> {
    public String postprocessorName() {
        return UUID.randomUUID().toString();
    }
}
