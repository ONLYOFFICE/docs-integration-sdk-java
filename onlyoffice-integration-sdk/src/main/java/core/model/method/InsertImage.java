package core.model.method;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.method.model.Image;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class InsertImage {
    private final String c;
    private final String fileType;
    private List<Image> images;
    @Deprecated
    @Setter
    private String url;
    @Setter
    private String token;

    /**
     *
     * @param c
     * @param fileType
     * @param images
     * @param url
     */
    public InsertImage(String c, String fileType, List<Image> images, String url) {
        this.c = c;
        this.fileType = fileType;
        this.images = images;
        this.url = url;
    }
}
