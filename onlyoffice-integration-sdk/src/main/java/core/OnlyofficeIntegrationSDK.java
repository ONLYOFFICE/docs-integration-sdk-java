package core;

import core.client.OnlyofficeCommandClient;
import core.client.OnlyofficeConverterClient;
import core.model.converter.request.ConverterRequest;
import core.runner.OnlyofficeCallbackRunner;
import core.runner.OnlyofficeEditorRunner;
import core.uploader.OnlyofficeUploaderRunner;
import lombok.RequiredArgsConstructor;

/**
 * <pre>ONLYOFFICE Default Pre-processors:</pre>
 * <h3>OnlyofficeEditorDefaultPreProcessor Invocation scheme:</h3>
 * { <br>
 *     <pre>&nbsp;"invocation_key": "onlyoffice.preprocessor.default.editor", </pre>
 *     <pre>&nbsp;"description": "", </pre>
 *     <pre>&nbsp;"type": "preprocessor", </pre>
 *     <pre>&nbsp;"parameters": { </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;"key": { </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"description": "", </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type": "String" </pre>
 *        <pre>&nbsp;&nbsp;&nbsp;}, </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;"token": { </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"description": "", </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type": "String" </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;}, </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;"mutator": { </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"description": "", </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"type": "OnlyofficeModelMutator<Config>" </pre>
 *         <pre>&nbsp;&nbsp;} </pre>
 *     <pre>&nbsp;}, </pre>
 *     <pre>&nbsp;"required": ["key", "token"] </pre>
 * } <br>
 * <br>
 * <h3>OnlyofficeCallbackDefaultPreProcessor Invocation scheme:</h3>
 * { <br>
 *     <pre>&nbsp;"invocation_key": "onlyoffice.preprocessor.default.callback", </pre>
 *     <pre>&nbsp;"description": "", </pre>
 *     <pre>&nbsp;"type": "preprocessor", </pre>
 *     <pre>&nbsp;"parameters": { </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;"key": { </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"description": "", </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type": "String" </pre>
 *        <pre>&nbsp;&nbsp;&nbsp;}, </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;"token": { </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"description": "", </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"type": "String" </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;}, </pre>
 *         <pre>&nbsp;&nbsp;&nbsp;"mutator": { </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"description": "", </pre>
 *             <pre>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp"type": "OnlyofficeModelMutator<Callback>" </pre>
 *         <pre>&nbsp;&nbsp;} </pre>
 *     <pre>&nbsp;}, </pre>
 *     <pre>&nbsp;"required": ["key", "token"] </pre>
 * } <br>
 */
@RequiredArgsConstructor
public class OnlyofficeIntegrationSDK {
    public final OnlyofficeCallbackRunner callbackRunner;
    public final OnlyofficeEditorRunner editorRunner;
    public final OnlyofficeUploaderRunner<ConverterRequest> converterRunner;
    public final OnlyofficeCommandClient commandClient;
    public final OnlyofficeConverterClient converterClient;
}
