package core.processor;

import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessRuntimeException;

public interface OnlyofficeProcessor<M> {
    /**
     *
     * @param model
     * @throws OnlyofficeProcessRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    void process(M model) throws OnlyofficeProcessRuntimeException, OnlyofficeInvalidParameterRuntimeException;
}
