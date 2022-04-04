package core;

import core.client.OnlyofficeCommandClient;
import core.client.OnlyofficeConverterClient;
import core.model.callback.Callback;
import core.model.config.Config;
import core.model.converter.request.ConverterRequest;
import core.runner.OnlyofficeRunner;
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
    public final OnlyofficeRunner<Callback> callbackRunner;
    public final OnlyofficeRunner<Config> editorRunner;
    public final OnlyofficeRunner<ConverterRequest> converterRunner;
    public final OnlyofficeCommandClient commandClient;
    public final OnlyofficeConverterClient converterClient;
}
