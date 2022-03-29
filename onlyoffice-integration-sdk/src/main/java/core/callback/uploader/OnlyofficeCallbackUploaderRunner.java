package core.callback.uploader;

import core.model.callback.Callback;

import java.io.IOException;

public interface OnlyofficeCallbackUploaderRunner {
    /**
     *
     * @param callback
     * @throws IOException
     */
    void run(Callback callback) throws IOException;
}
