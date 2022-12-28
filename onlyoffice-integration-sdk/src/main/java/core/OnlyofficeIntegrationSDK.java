package core;

import core.client.OnlyofficeCommandClient;
import core.client.OnlyofficeConverterClient;
import core.model.converter.request.ConverterRequest;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.OnlyofficeEditorRunner;
import core.uploader.OnlyofficeUploaderRunner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OnlyofficeIntegrationSDK {
    public final OnlyofficeCallbackRunner callbackRunner;
    public final OnlyofficeEditorRunner editorRunner;
    public final OnlyofficeCommandClient commandClient;
    public final OnlyofficeConverterClient converterClient;
}
