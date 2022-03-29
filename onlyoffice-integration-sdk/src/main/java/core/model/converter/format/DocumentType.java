package core.model.converter.format;

public enum DocumentType {
    WORD("word"),
    CELL("cell"),
    SLIDE("slide");
    DocumentType(String type) {
        this.type = type;
    }
    private String type;
    public String getType() {
        return type;
    }
}
