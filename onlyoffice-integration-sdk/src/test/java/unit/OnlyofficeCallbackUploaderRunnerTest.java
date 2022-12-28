package unit;

import base.uploader.OnlyofficeDefaultCallbackUploaderRunner;
import core.model.callback.Callback;
import core.uploader.OnlyofficeUploader;
import core.uploader.OnlyofficeUploaderRunner;
import exception.OnlyofficeUploaderRuntimeException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OnlyofficeCallbackUploaderRunnerTest {
    private String fileURL = System.getenv("file_url");
    private final OnlyofficeUploaderRunner<Callback> callbackUploaderRunner = new OnlyofficeDefaultCallbackUploaderRunner(
            List.of(new OnlyofficeUploader<Callback>() {
                public void upload(Callback model, InputStream stream) throws OnlyofficeUploaderRuntimeException {

                }
            })
    );

    @Test
    public void processUploaderInvalidURL() {
        assertThrows(MalformedURLException.class, () -> callbackUploaderRunner.run(
                Callback
                        .builder()
                        .build()
        ));
    }

    @Test
    public void processUploaderOpenFileStream() {
        assertDoesNotThrow(() -> callbackUploaderRunner.run(
                Callback
                        .builder()
                        .url(fileURL)
                        .build()
        ));
    }
}
