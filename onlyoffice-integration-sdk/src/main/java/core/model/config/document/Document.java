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
    private String fileType;
    @NotBlank(message = "ONLYOFFICE Document config: key can't be blank")
    @Length(min = 4)
    private String key;
    @NotBlank(message = "ONLYOFFICE Document config: title can't be blank")
    @Length(min = 4)
    private String title;
    @NotBlank(message = "ONLYOFFICE Document config: url can't be blank")
    @Length(min = 11)
    private String url;
    private Info info;
    private Permissions permissions;
}
