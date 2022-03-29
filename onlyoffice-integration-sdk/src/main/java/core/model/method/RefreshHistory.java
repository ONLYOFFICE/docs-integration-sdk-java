package core.model.method;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.callback.History;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class RefreshHistory {
    private final int currentVersion;
    private final List<History> history;
    @Setter
    private String error;
}
