package core.uploader;

import exception.OnlyofficeUploaderRuntimeException;

import java.io.InputStream;

public interface OnlyofficeUploader<M> {
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
    default OnlyofficeUploaderType getUploaderType() {
        return OnlyofficeUploaderType.FILE;
    }
}
