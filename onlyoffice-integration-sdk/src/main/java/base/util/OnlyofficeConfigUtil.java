package base.util;

import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.model.converter.format.Format;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeFile;
import exception.OnlyofficeInvalidParameterRuntimeException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OnlyofficeConfigUtil implements OnlyofficeConfig {
    private OnlyofficeFile fileUtil;

    /**
     *
     * @param config
     * @throws OnlyofficeInvalidParameterRuntimeException
     */
    public void sanitizeConfig(Config config) throws OnlyofficeInvalidParameterRuntimeException {
        Document document = config.getDocument();
        Editor editor = config.getEditorConfig();
        try {
            String fileType = (document.getFileType() == null || document.getFileType().isBlank()) ?
                    this.fileUtil.findFileType(document.getTitle()) : document.getFileType();
            document.setFileType(fileType);

            String lang = (editor.getLang() == null || editor.getLang().isBlank()) ?
                    "en" : editor.getLang();
            editor.setLang(lang);

            String documentType = (config.getDocumentType() == null || config.getDocumentType().isBlank()) ?
                    this.fileUtil.findDocumentType(document.getTitle()).getType() : config.getDocumentType();
            config.setDocumentType(documentType);

            Format format = Format.getFormatByExtension(fileType);

            if (format != null) editor.setMode(format.getEditable() ? "edit" : "view");
        } catch (Exception e) {
            throw new OnlyofficeInvalidParameterRuntimeException("Could not sanitize ONLYOFFICE configuration");
        }
    }
}
