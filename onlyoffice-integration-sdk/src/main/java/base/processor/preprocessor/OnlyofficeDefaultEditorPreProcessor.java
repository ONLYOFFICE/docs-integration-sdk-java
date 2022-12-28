package base.processor.preprocessor;

import core.model.config.Config;
import core.processor.OnlyofficeEditorPreProcessor;
import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;

public class OnlyofficeDefaultEditorPreProcessor implements OnlyofficeEditorPreProcessor {
    @Override
    public void processBefore(Config model) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException {}
}
