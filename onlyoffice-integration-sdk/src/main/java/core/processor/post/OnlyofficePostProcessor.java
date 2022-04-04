package core.processor.post;

import exception.OnlyofficeInvalidParameterRuntimeException;
import exception.OnlyofficeProcessAfterRuntimeException;

interface OnlyofficePostProcessor<M> {

    /**
     *
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    default void processAfter() throws OnlyofficeProcessAfterRuntimeException, OnlyofficeInvalidParameterRuntimeException {}

    /**
     *
     * @param model
     * @throws OnlyofficeProcessAfterRuntimeException
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    default void processAfter(M model) throws OnlyofficeProcessAfterRuntimeException, OnlyofficeInvalidParameterRuntimeException {}

    /**
     *
     * @return
     */
    String postprocessorName();
}
