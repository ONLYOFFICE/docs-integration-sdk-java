package core.model.config.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class SharingSettings {
    private Boolean isLink;
    private SharingSettingsPermission permissions;
    private String user;
}
