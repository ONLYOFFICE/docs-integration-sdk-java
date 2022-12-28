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
    private Boolean chat;
    private Boolean comment;
    private List<CommentGroup> commentGroups;
    private Boolean copy;
    private Boolean deleteCommentAuthorOnly;
    private Boolean download;
    private Boolean edit;
    private Boolean editCommentAuthorOnly;
    private Boolean fillForms;
    private Boolean modifyContentControl;
    private Boolean modifyFilter;
    private Boolean print;
    private Boolean protect;
    private Boolean rename;
    private Boolean review;
    private List<String> reviewGroups;
    private List<String> userInfoGroups;
}
