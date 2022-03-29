package core.model.method;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class RevisedFile {
    private final String fileType;
    private final String url;
    @Setter
    private String token;
}
