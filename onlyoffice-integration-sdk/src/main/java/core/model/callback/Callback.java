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
    @Setter(AccessLevel.NONE)
    private String token;
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private String secret;
    private Integer forcesavetype;
    private Integer status;
    private List<String> users;
    private List<Action> actions;
    private String userdata;
    private String lastsave;
    private Boolean notmodified;
    @JsonIgnore
    @Builder.Default
    private Map<String, Object> custom = new HashMap<>();

    public Callback setToken(String token) {
        this.token = token;
        return this;
    }

    public Callback setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public Callback setCustom(Map<String, Object> custom) {
        this.custom = custom;
        return this;
    }
}
