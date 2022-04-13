package core.runner;

import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;

import java.io.IOException;

interface OnlyofficeRunner<M, R> {
    /**
     *
     * @param model
     * @throws OnlyofficeRunnerRuntimeException
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    R run(M model) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException;
}
