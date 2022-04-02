package core.uploader;

import java.io.InputStream;

public interface OnlyofficeFileUploader<T> {
    /**
     *
     * @param callback
     * @param stream
     */
    void upload(T callback, InputStream stream);

    /**
     *
     * @return
     */
    OnlyofficeFileUploaderType getType();
}
