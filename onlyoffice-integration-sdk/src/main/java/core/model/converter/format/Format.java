package core.model.converter.format;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Format {
    DOC("doc", "application/msword", DocumentType.WORD,
            false, List.of("docx", "odt", "pdf", "rtf", "txt")),
    DOCM("docm", "application/vnd.ms-word.document.macroEnabled.12", DocumentType.WORD,
            false, List.of("docx", "odt", "pdf", "rtf", "txt")),
    DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", DocumentType.WORD,
            true, List.of("odt", "pdf", "rtf", "txt")),
    DOCXF("docxf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document.docxf", DocumentType.WORD,
            true, List.of("odt", "pdf", "rtf", "txt")),
    OFORM("oform", "application/vnd.openxmlformats-officedocument.wordprocessingml.document.oform", DocumentType.WORD,
            false, null),
    DOT("dot", "text/vnd.graphviz",  DocumentType.WORD,
            false, List.of("docx", "odt", "pdf", "rtf", "txt")),
    DOTX("dotx","application/vnd.openxmlformats-officedocument.wordprocessingml.template", DocumentType.WORD,
            false, List.of("docx", "odt", "pdf", "rtf", "txt")),
    EPUB("epub", "application/epub+zip", DocumentType.WORD,
            false, List.of("docx", "odt", "pdf", "rtf", "txt")),
    HTML("html", "text/html", DocumentType.WORD,
            false, List.of("docx", "odt", "pdf", "rtf", "txt")),
    ODT("odt", "application/vnd.oasis.opendocument.text", DocumentType.WORD,
            true, List.of("docx", "pdf", "rtf", "txt")),
    OTT("ott", "application/vnd.oasis.opendocument.text-template", DocumentType.WORD,
            false, List.of("docx", "odt", "pdf", "rtf", "txt")),
    PDF("pdf","application/pdf", DocumentType.WORD,
            false, null),
    RTF("rtf", "text/rtf", DocumentType.WORD,
            true, List.of("docx", "odt", "pdf", "txt")),
    TXT("txt", "text/plain", DocumentType.WORD,
            true, List.of("docx", "odt", "pdf", "rtf")),
    CSV("csv", "text/csv", DocumentType.CELL,
            true, List.of("ods", "pdf", "xlsx")),
    ODS("ods", "application/vnd.oasis.opendocument.spreadsheet", DocumentType.CELL,
            true, List.of("csv", "pdf", "xlsx")),
    OTS("ots", "application/vnd.oasis.opendocument.spreadsheet-template", DocumentType.CELL,
            false, List.of("csv", "ods", "pdf", "xlsx")),
    XLS("xls", "application/vnd.ms-excel", DocumentType.CELL,
            false, List.of("csv", "ods", "pdf", "xlsx")),
    XLSM("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12", DocumentType.CELL,
            false, List.of("csv", "ods", "pdf", "xlsx")),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", DocumentType.CELL,
            true, List.of("csv", "ods", "pdf")),
    XLTM("xltm", "application/vnd.ms-excel.template.macroEnabled.12", DocumentType.CELL,
            false, List.of("csv", "ods", "pdf", "xlsx")),
    XLTX("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template", DocumentType.CELL,
            false, List.of("csv", "ods", "pdf", "xlsx")),
    ODP("odp", "application/vnd.oasis.opendocument.presentation", DocumentType.SLIDE,
            true, List.of("pdf", "pptx")),
    POT("pot", "application/powerpoint", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    POTM("potm", "application/vnd.ms-powerpoint.template.macroEnabled.12", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    POTX("potx", "application/vnd.openxmlformats-officedocument.presentationml.template", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    PPS("pps", "application/vnd.ms-powerpoint", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    PPSM("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    PPSX("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    PPT("ppt", "application/vnd.ms-powerpoint", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    PPTM("pptm","application/vnd.ms-powerpoint.presentation.macroEnabled.12", DocumentType.SLIDE,
            false, List.of("pdf", "pptx", "odp")),
    PPTX("pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation", DocumentType.SLIDE,
            true, List.of("pdf", "odp"));
    Format(String extension, String mime, DocumentType type, Boolean editable, List<String> saveas) {
        this.extension = extension;
        this.mime = mime;
        this.type = type;
        this.editable = editable;
        this.saveas = saveas;
    }
    private static Map<String, Format> formatMap = new HashMap<>(){{
        put("doc", DOC);
        put("docm", DOCM);
        put("docx", DOCX);
        put("docxf", DOCXF);
        put("oform", OFORM);
        put("dot", DOT);
        put("dotx", DOTX);
        put("epub", EPUB);
        put("html", HTML);
        put("odt", ODT);
        put("ott", OTT);
        put("pdf", PDF);
        put("rtf", RTF);
        put("txt", TXT);
        put("csv", CSV);
        put("ods", ODS);
        put("ots", OTS);
        put("xls", XLS);
        put("xlsm", XLSM);
        put("xlsx", XLSX);
        put("xltm", XLTM);
        put("xltx", XLTX);
        put("odp", ODP);
        put("pot", POT);
        put("potm", POTM);
        put("potx", POTX);
        put("pps", PPS);
        put("ppsm", PPSM);
        put("ppsx", PPSX);
        put("ppt", PPT);
        put("pptm", PPTM);
        put("pptx", PPTX);
    }};
    private String extension;
    @JsonIgnore
    private String mime;
    @JsonIgnore
    private DocumentType type;
    @JsonIgnore
    private Boolean editable;
    @JsonIgnore
    private List<String> saveas;
    public String getExtension() {
        return extension;
    }
    public String getMime() {
        return mime;
    }
    public DocumentType getType() {
        return type;
    }
    public Boolean getEditable() {
        return editable;
    }
    public List<String> getConvertTypes() {
        return saveas;
    }
    public static Format getFormatByExtension(String ext) {
        return Format.formatMap.get(ext);
    }
}
