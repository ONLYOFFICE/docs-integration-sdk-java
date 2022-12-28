package base.processor.postprocessor;

import core.model.callback.Callback;
import core.processor.OnlyofficeCallbackPostProcessor;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessAfterRuntimeException;

public class OnlyofficeDefaultCallbackPostProcessor implements OnlyofficeCallbackPostProcessor {
    public void processAfter(Callback callback) throws OnlyofficeProcessAfterRuntimeException, OnlyofficeInvalidParameterRuntimeException {}
}
