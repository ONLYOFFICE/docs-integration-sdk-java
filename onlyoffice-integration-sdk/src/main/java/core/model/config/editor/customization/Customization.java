package core.model.config.editor.customization;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.config.editor.customization.enums.MacrosMode;
import core.model.config.editor.customization.enums.ReviewDisplay;
import core.model.config.editor.customization.enums.Unit;
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
    private Boolean autosave;
    private Boolean chat;
    private Boolean commentAuthorOnly;
    private Boolean comments;
    private Boolean compactHeader;
    private Boolean compactToolbar;
    private Boolean compatibleFeatures;
    private Customer customer;
    private Features features;
    private Boolean feedback;
    private Boolean forcesave;
    private Goback goback;
    private Boolean help;
    private Boolean hideNotes;
    private Boolean hideRightMenu;
    private Boolean hideRulers;
    private String integrationMode;
    private Logo logo;
    private Boolean macros;
    private MacrosMode macrosMode;
    private Boolean mentionShare;
    private Boolean plugins;
    private Review review;
    @Deprecated
    private ReviewDisplay reviewDisplay;
    @Deprecated
    private Boolean showReviewChanges;
    @Deprecated
    private Boolean spellcheck;
    private Boolean toolbarHideFileName;
    private Boolean toolbarNoTabs;
    @Deprecated
    private Boolean trackChanges;
    private String uiTheme;
    private Unit unit;
    private Integer zoom;
}