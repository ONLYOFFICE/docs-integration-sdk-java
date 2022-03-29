package core.model.command.license.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.command.CommandAuthorizedBase;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.net.URI;

@Getter
@Builder
@ToString
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class CommandLicenseRequest extends CommandAuthorizedBase {
    @Builder.Default
    private final String c = "license";
    @JsonIgnore
    @NotNull
    private URI address;
}
