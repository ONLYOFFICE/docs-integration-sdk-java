package core.runner;

import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;

import java.io.IOException;

interface OnlyofficeRunner<M> {
    /**
     *
     * @param model
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    void run(M model) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException;
}
