package core.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Callback {
    private String filetype;
    private String url;
    private String key;
    private String changesurl;
    private History history;
    private String token;
    private Integer forcesavetype;
    private Integer status;
    private List<String> users;
    private List<Action> actions;
    private String userdata;
    private String lastsave;
    private Boolean notmodified;
    @JsonIgnore
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Map<String, ?> custom = new HashMap<>();

    public Callback setCustom(Map<String, ?> custom) {
        this.custom = custom;
        return this;
    }
}
