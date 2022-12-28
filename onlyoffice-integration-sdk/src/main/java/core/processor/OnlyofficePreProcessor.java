package core.processor;

import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;

public interface OnlyofficePreProcessor<M> {
    /**
     *
     * @param model
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    void processBefore(M model) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException;
}
