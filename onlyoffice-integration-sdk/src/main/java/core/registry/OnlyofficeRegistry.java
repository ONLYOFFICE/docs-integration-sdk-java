package core.registry;

import exception.OnlyofficeRegistryHandlerRuntimeException;
import exception.OnlyofficeRegistryRuntimeException;

interface OnlyofficeRegistry<M, C, H extends OnlyofficeRegistryHandler<M, C>> {
    /**
     *
     * @param handlers
     */
    void register(H... handlers);

    /**
     *
     * @param code
     */
    void removeByCode(C code);

    /**
     *
     * @param model
     * @throws OnlyofficeRegistryRuntimeException
     * @throws OnlyofficeRegistryHandlerRuntimeException
     */
    void run(M model) throws OnlyofficeRegistryRuntimeException, OnlyofficeRegistryHandlerRuntimeException;

    /**
     *
     * @return
     */
    int registered();
}
