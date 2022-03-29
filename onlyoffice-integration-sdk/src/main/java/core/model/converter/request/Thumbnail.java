package core.model.converter.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Thumbnail {
    private int aspect;
    private Boolean first;
    private int height;
    private int width;
}
