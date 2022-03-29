package core.model.config.editor.customization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Logo {
    private String image;
    private String imageDark;
    private String imageEmbedded;
    private String url;
}