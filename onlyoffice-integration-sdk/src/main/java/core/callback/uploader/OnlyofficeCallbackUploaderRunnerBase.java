package core.callback.uploader;

import core.model.callback.Callback;
import core.uploader.OnlyofficeFileUploader;
import core.uploader.OnlyofficeFileUploaderType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class OnlyofficeCallbackUploaderRunnerBase implements OnlyofficeCallbackUploaderRunner {
    private List<OnlyofficeCallbackUploader> uploaders;

    /**
     *
     * @param uploaders
     */
    public OnlyofficeCallbackUploaderRunnerBase(List<OnlyofficeCallbackUploader> uploaders) {
        this.uploaders = uploaders;
        this.uploaders.sort(
                (u1, u2) -> u1.getType().equals(u2.getType()) ? 0 : u1.getType().equals(OnlyofficeFileUploaderType.FILE) ? -1 : 1
        );
    }

    /**
     *
     * @param callback
     * @throws IOException
     */
    public void run(Callback callback) throws IOException {
        for (OnlyofficeFileUploader uploader : uploaders) {
            doUpload(callback, uploader);
        }
    }

    /**
     *
     * @param callback
     * @param uploader
     * @throws IOException
     */
    private void doUpload(Callback callback, OnlyofficeFileUploader uploader) throws IOException {
        if (uploader.getType().equals(OnlyofficeFileUploaderType.FILE))
            this.doBasicUpload(new URL(callback.getUrl()), callback, uploader);
        if (uploader.getType().equals(OnlyofficeFileUploaderType.DIFF))
            this.doBasicUpload(new URL(callback.getChangesurl()), callback, uploader);
    }

    /**
     *
     * @param url
     * @param callback
     * @param uploader
     * @throws IOException
     */
    private void doBasicUpload(URL url, Callback callback, OnlyofficeFileUploader uploader) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            uploader.upload(callback, inputStream);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
