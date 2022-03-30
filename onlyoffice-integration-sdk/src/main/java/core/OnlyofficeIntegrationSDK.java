package core;

import client.OnlyofficeCommandClient;
import client.OnlyofficeConverterClient;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.OnlyofficeEditorRunner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OnlyofficeIntegrationSDK {
    private final OnlyofficeCallbackRunner callbackRunner;
    private final OnlyofficeEditorRunner editorRunner;
    private final OnlyofficeCommandClient commandClient;
    private final OnlyofficeConverterClient converterClient;
}
