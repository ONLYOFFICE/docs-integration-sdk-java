package com.onlyoffice.springintegrationlib.extension;

import core.callback.OnlyofficeCallbackHandler;
import core.callback.OnlyofficeCallbackRegistry;
import org.springframework.beans.factory.annotation.Autowired;

public interface AutoInjectableOnlyofficeCallbackHandler extends OnlyofficeCallbackHandler {
    @Autowired
    default void selfRegister(OnlyofficeCallbackRegistry callbackRegistry) {
        callbackRegistry.registerCallbacks(this);
    }
}
