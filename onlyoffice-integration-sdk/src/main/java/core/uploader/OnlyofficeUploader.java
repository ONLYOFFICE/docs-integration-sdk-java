package core.uploader;

import exception.OnlyofficeUploaderRuntimeException;

import java.io.InputStream;

interface OnlyofficeUploader<M> {
    /**
     *
     * @param model
     * @param stream
     * @throws OnlyofficeUploaderRuntimeException
     */
    void upload(M model, InputStream stream) throws OnlyofficeUploaderRuntimeException;

    /**
     *
     * @return
     */
    OnlyofficeUploaderType getUploaderType();
}
