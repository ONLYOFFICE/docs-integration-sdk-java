package core.processor;

import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;

public interface OnlyofficePreProcessor<M> {
    /**
     *
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    default void processBefore() throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {};

    /**
     *
     * @param model
     * @throws OnlyofficeProcessBeforeRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    default void processBefore(M model) throws OnlyofficeProcessBeforeRuntimeException, OnlyofficeInvalidParameterRuntimeException {};

    /**
     *
     * @return
     */
    String preprocessorName();
}
