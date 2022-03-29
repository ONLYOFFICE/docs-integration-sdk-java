package core.model.method;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class PreviousHistoryData {
    private final String key;
    private final String url;
    @Setter
    private String fileType;
}
