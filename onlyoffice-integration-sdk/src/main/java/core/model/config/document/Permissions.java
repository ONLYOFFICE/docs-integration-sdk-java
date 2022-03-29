package core.model.config.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString
public class Permissions {
    private Boolean changeHistory;
    private Boolean comment;
    private Boolean copy;
    private Boolean deleteCommentAuthorOnly;
    private Boolean download;
    private Boolean edit;
    private Boolean editCommentAuthorOnly;
    private Boolean print;
    private Boolean fillForms;
    private Boolean modifyFilter;
    private Boolean modifyContentControl;
    private Boolean protect;
    private Boolean rename;
    private Boolean review;
    private List<String> reviewGroups;
    private List<CommentGroup> commentGroups;
}
