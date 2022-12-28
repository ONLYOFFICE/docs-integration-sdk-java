package core.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Callback {
    private List<Action> actions;
    private List<ChangesHistory> changeshistory;
    private String changesurl;
    private String filetype;
    private Integer forcesavetype;
    private History history;
    private String key;
    private Integer status;
    private String url;
    private String userdata;
    private List<String> users;
    @Setter(AccessLevel.NONE)
    private String token;
    @JsonIgnore
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private JSONObject extras = new JSONObject();

    public Callback setToken(String token) {
        this.token = token;
        return this;
    }
}
