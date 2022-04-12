package core.registry;

import exception.OnlyofficeRegistryHandlerRuntimeException;

interface OnlyofficeRegistryHandler<M, C> {
    /**
     *
     * @param model
     * @throws OnlyofficeRegistryHandlerRuntimeException
     */
    void handle(M model) throws OnlyofficeRegistryHandlerRuntimeException;

    /**
     *
     * @return
     */
    C getCode();
}
