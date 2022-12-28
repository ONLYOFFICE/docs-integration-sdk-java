package core.model.config.editor;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.common.User;
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
    @NotBlank(message = "ONLYOFFICE Editor config: callback url can't be blank")
    @Length(min = 11)
    private String callbackUrl;
    private CoEditing coEditing;
    private String createUrl;
    private String lang;
    private String location;
    private String mode;
    private List<Recent> recent;
    private String region;
    private List<Template> templates;
    private User user;
    private Customization customization;
    private Embedded embedded;
    private Plugin plugins;
}
