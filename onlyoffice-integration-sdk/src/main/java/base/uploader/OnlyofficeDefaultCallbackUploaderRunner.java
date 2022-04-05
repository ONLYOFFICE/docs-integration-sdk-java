package base.uploader;

import core.model.callback.Callback;
import core.uploader.OnlyofficeUploader;
import core.uploader.OnlyofficeUploaderRunner;
import core.uploader.OnlyofficeUploaderType;
import exception.OnlyofficeRunnerRuntimeException;
import exception.OnlyofficeUploaderRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class OnlyofficeDefaultCallbackUploaderRunner implements OnlyofficeUploaderRunner<Callback> {
    private List<OnlyofficeUploader<Callback>> uploaders;

    public OnlyofficeDefaultCallbackUploaderRunner(List<OnlyofficeUploader<Callback>> uploaders) {
        this.uploaders = uploaders;
        this.uploaders.sort(
                (u1, u2) -> u1.getUploaderType().equals(u2.getUploaderType()) ? 0 : u1.getUploaderType().equals(OnlyofficeUploaderType.FILE) ? - 1 : 1
        );
    }

    /**
     *
     * @param callback
     * @throws OnlyofficeUploaderRuntimeException
     * @throws IOException
     */
    public void run(Callback callback) throws OnlyofficeUploaderRuntimeException, IOException {
        for (OnlyofficeUploader<Callback> uploader : uploaders) {
            doUpload(callback, uploader);
        }
    }

    /**
     *
     * @param callback
     * @param uploader
     * @throws OnlyofficeRunnerRuntimeException
     * @throws IOException
     */
    private void doUpload(Callback callback, OnlyofficeUploader<Callback> uploader) throws OnlyofficeRunnerRuntimeException, IOException {
        if (uploader.getUploaderType().equals(OnlyofficeUploaderType.FILE))
            this.doBasicUpload(new URL(callback.getUrl()), callback, uploader);
        if (uploader.getUploaderType().equals(OnlyofficeUploaderType.DIFF))
            this.doBasicUpload(new URL(callback.getChangesurl()), callback, uploader);
    }

    /**
     *
     * @param url
     * @param callback
     * @param uploader
     * @throws IOException
     */
    private void doBasicUpload(URL url, Callback callback, OnlyofficeUploader<Callback> uploader) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            uploader.upload(callback, inputStream);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
