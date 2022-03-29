package core.model.config.editor;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.User;
import core.model.config.editor.customization.Customization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Editor {
    private HashMap<String, Object> actionLink;
    private String mode;
    private String lang;
    private String location;
    private String region;
    @NotBlank(message = "ONLYOFFICE Editor config: callback url can't be blank")
//    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")
    @Length(min = 11)
    private String callbackUrl;
    private String createUrl;
    private List<Template> templates;
    private User user;
    private Embedded embedded;
    private Customization customization;
    private CoEditing coEditing;
    private Plugin plugins;
}
