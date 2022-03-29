package core.model.config.editor.customization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Goback {
    private Boolean blank;
    private Boolean requestClose;
    private String text;
    private String url;
}
