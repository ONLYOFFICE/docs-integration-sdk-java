package core.model.method;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class HistoryData {
    private final String key;
    private final String url;
    private final int version;

    @Setter
    private PreviousHistoryData previous;
    @Setter
    private String changesUrl;
    @Setter
    private String error;
    @Setter
    private String fileType;
    @Setter
    private String token;
}
