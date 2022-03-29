package core.model.config.editor.customization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Review {
    private Boolean hideReviewDisplay;
    private Boolean hoverMode;
    private String reviewDisplay;
    private Boolean showReviewChanges;
    private Boolean trackChanges;
}
