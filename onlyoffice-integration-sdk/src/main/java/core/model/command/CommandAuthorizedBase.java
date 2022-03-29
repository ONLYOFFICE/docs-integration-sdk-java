package core.model.command;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@Setter
@Getter
public class CommandAuthorizedBase {
    private String token;
}
