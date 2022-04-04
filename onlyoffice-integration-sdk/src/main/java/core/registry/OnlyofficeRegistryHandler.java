package core.registry;

import exception.OnlyofficeRegistryHandlerRuntimeException;

interface OnlyofficeRegistryHandler<M, C> {
    /**
     *
     * @param callback
     * @throws OnlyofficeRegistryHandlerRuntimeException
     */
    void handle(M callback) throws OnlyofficeRegistryHandlerRuntimeException;

    C getCode();
}
