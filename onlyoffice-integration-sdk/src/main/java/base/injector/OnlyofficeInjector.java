package base.injector;

import base.handler.OnlyofficeForceSaveCallbackHandler;
import base.handler.OnlyofficeSaveCallbackHandler;
import base.processor.postprocessor.OnlyofficeDefaultCallbackPostProcessor;
import base.processor.postprocessor.OnlyofficeEditorJwtPostProcessor;
import base.processor.preprocessor.OnlyofficeCallbackJwtPreProcessor;
import base.processor.preprocessor.OnlyofficeDefaultEditorPreProcessor;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import core.processor.OnlyofficeCallbackPostProcessor;
import core.processor.OnlyofficeCallbackPreProcessor;
import core.processor.OnlyofficeEditorPostProcessor;
import core.processor.OnlyofficeEditorPreProcessor;
import core.registry.OnlyofficeCallbackHandler;

public class OnlyofficeInjector extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<OnlyofficeCallbackHandler> onlyofficeCallbackHandlerBinder =
                Multibinder.newSetBinder(binder(), OnlyofficeCallbackHandler.class);
        onlyofficeCallbackHandlerBinder.addBinding().to(OnlyofficeForceSaveCallbackHandler.class);
        onlyofficeCallbackHandlerBinder.addBinding().to(OnlyofficeSaveCallbackHandler.class);

        Multibinder<OnlyofficeCallbackPreProcessor> onlyofficeCallbackPreProcessorBinder =
                Multibinder.newSetBinder(binder(), OnlyofficeCallbackPreProcessor.class);
        onlyofficeCallbackPreProcessorBinder.addBinding().to(OnlyofficeCallbackJwtPreProcessor.class);

        Multibinder<OnlyofficeCallbackPostProcessor> onlyofficeCallbackPostProcessorBinder =
                Multibinder.newSetBinder(binder(), OnlyofficeCallbackPostProcessor.class);
        onlyofficeCallbackPostProcessorBinder.addBinding().to(OnlyofficeDefaultCallbackPostProcessor.class);

        Multibinder<OnlyofficeEditorPreProcessor> onlyofficeEditorPreProcessorBinder =
                Multibinder.newSetBinder(binder(), OnlyofficeEditorPreProcessor.class);
        onlyofficeEditorPreProcessorBinder.addBinding().to(OnlyofficeDefaultEditorPreProcessor.class);

        Multibinder<OnlyofficeEditorPostProcessor> onlyofficeEditorPostProcessorBinder =
                Multibinder.newSetBinder(binder(), OnlyofficeEditorPostProcessor.class);
        onlyofficeEditorPostProcessorBinder.addBinding().to(OnlyofficeEditorJwtPostProcessor.class);
    }
}
