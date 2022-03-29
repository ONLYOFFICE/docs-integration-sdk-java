package core.model.command.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.command.CommandAuthorizedBase;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.net.URI;

@Getter
@Builder
@ToString
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class CommandMetaRequest extends CommandAuthorizedBase {
    private final String c = "meta";
    @JsonIgnore
    @NotNull
    private URI address;
    @NotNull
    @Length(min = 3)
    private String key;
    @NotNull
    private Meta meta;
}
