package core.processor;

import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;

public interface OnlyofficePostProcessor<M> {
    /**
     *
     * @param model
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    void processAfter(M model) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException;
}
