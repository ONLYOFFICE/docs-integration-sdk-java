package core.model.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CommandInfoRequest extends CommandAuthorizedBase {
    @Builder.Default
    private final String c = "info";
    @JsonIgnore
    @NotNull
    private URI address;
    @NotNull
    @Length(min = 3)
    private String key;
}
