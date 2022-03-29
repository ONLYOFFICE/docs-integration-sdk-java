package core.uploader;

import java.io.InputStream;

public interface OnlyofficeFileUploader<T> {
    void upload(T callback, InputStream stream);
    OnlyofficeFileUploaderType getType();
}
