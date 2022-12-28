package core.model.converter.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.converter.format.Format;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Getter
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class ConverterRequest {
    @JsonIgnore
    @NotNull
    private URI address;
    @Setter
    private Boolean async;
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private int codePage;
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private int delimiter;
    @NotNull
    private Format filetype;
    @NotNull
    @NotBlank
    private String key;
    @NotNull
    private Format outputtype;
    private String password;
    private String region;
    private Thumbnail thumbnail;
    private String title;
    @Setter
    private String token;
    @Length(min = 11)
    private String url;
}
