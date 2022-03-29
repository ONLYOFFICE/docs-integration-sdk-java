package core.model.config.editor;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Embedded {
    private String embedUrl;
    private String fullscreenUrl;
    private String saveUrl;
    private String shareUrl;
    private String toolbarDocked;
}
