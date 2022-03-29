package core.model.config.editor.customization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Customization {
    private Anonymous anonymous;
    private Logo logo;
    private Goback goback;
    private Boolean autosave;
    private Boolean chat;
    private Boolean commentAuthorOnly;
    private Boolean comments;
    private Boolean compactHeader;
    private Boolean compactToolbar;
    private Boolean compatibleFeatures;
    private Customer customer;
    private Boolean forcesave;
    private Boolean help;
    private Boolean hideNotes;
    private Boolean hideRightMenu;
    private Boolean hideRulers;
    private Boolean macros;
    private Boolean mentionShare;
    private Boolean plugins;
    private Review review;
    @Deprecated
    private String reviewDisplay;
    @Deprecated
    private Boolean showReviewChanges;
    private Boolean spellcheck;
    private Boolean toolbarHideFileName;
    private Boolean toolbarNoTabs;
    @Deprecated
    private Boolean trackChanges;
    private String uiTheme;
    private String unit;
    private Integer zoom;
    private Boolean submitForm;
}