package base.runner.callback;

import core.model.callback.Callback;
import core.runner.OnlyofficeRunner;
import exception.OnlyofficeProcessAfterRuntimeException;
import exception.OnlyofficeProcessBeforeRuntimeException;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;

import java.io.IOException;

public class OnlyofficeExperimentalCallbackRunner implements OnlyofficeRunner<Callback> {
    public void run(Callback model) throws OnlyofficeRunnerRuntimeException, OnlyofficeProcessBeforeRuntimeException, OnlyofficeProcessAfterRuntimeException, OnlyofficeUploaderRuntimeException, IOException {

    }
}
