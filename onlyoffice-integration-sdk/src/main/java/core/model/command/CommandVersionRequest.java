package core.model.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.net.URI;

@Getter
@Builder
@ToString
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class CommandVersionRequest extends CommandAuthorizedBase {
    @Builder.Default
    private final String c = "version";
    @JsonIgnore
    @NotNull
    private URI address;
}
