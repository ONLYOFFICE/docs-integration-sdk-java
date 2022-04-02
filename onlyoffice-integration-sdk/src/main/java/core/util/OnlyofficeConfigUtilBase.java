package core.util;

import core.model.config.Config;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import core.model.converter.format.Format;
import exception.OnlyofficeEditorConfigurationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OnlyofficeConfigUtilBase implements OnlyofficeConfigUtil {
    private OnlyofficeFileUtil fileUtil;

    /**
     *
     * @param config
     * @return
     * @throws OnlyofficeEditorConfigurationException
     */
    public Config sanitizeConfig(Config config) throws OnlyofficeEditorConfigurationException {
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
            throw new OnlyofficeEditorConfigurationException("Could not sanitize ONLYOFFICE configuration");
        }
        return config;
    }
}
