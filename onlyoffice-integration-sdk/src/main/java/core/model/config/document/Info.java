package core.model.config.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Info {
    private String author;
    private String created;
    private Boolean favorite;
    private String folder;
    private String owner;
    private List<SharingSettings> sharingSettings;
    private String uploaded;
}
