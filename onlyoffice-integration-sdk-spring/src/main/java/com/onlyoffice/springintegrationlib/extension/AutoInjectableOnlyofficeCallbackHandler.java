package com.onlyoffice.springintegrationlib.extension;

import core.registry.OnlyofficeCallbackHandler;
import core.registry.OnlyofficeCallbackRegistry;
import org.springframework.beans.factory.annotation.Autowired;

public interface AutoInjectableOnlyofficeCallbackHandler extends OnlyofficeCallbackHandler {
    @Autowired
    default void selfRegister(OnlyofficeCallbackRegistry callbackRegistry) {
        callbackRegistry.register(this);
    }
}
