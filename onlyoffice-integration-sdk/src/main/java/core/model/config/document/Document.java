package core.model.config.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString
public class Document {
    @NotBlank(message = "ONLYOFFICE Document config: title can't be blank")
    @Length(min = 4)
    private String title;
    @NotBlank(message = "ONLYOFFICE Document config: url can't be blank")
//    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
    @Length(min = 11)
    private String url;
    private String fileType;
    @NotBlank(message = "ONLYOFFICE Document config: key can't be blank")
    @Length(min = 4)
    private String key;
    private Info info;
    private Permissions permissions;
}
