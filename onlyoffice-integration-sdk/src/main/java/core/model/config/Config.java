package core.model.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.config.document.Document;
import core.model.config.editor.Editor;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Config {
    private String width;
    private String height;
    private String type;
    private String documentType;
    private String token;
    @JsonIgnore
    private String secret;
    @NotNull(message = "ONLYOFFICE Config: document configuration can't be null")
    @Valid
    @Builder.Default
    private Document document = new Document();
    @NotNull(message = "ONLYOFFICE Config: editor configuration can't be null")
    @Valid
    @Builder.Default
    private Editor editorConfig = new Editor();
    @JsonIgnore
    @Builder.Default
    private Map<String, Object> custom = new HashMap<>();
}
