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
    @Setter(AccessLevel.NONE)
    private Map<String, Map<String, Object>> processors = new HashMap<>();
    @JsonIgnore
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private Map<String, ?> custom = new HashMap<>();

    public Callback setToken(String token) {
        this.token = token;
        return this;
    }

    public Callback setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public Callback setProcessors(Map<String, Map<String, Object>> processors) {
        this.processors = processors;
        return this;
    }

    public Callback setCustom(Map<String, ?> custom) {
        this.custom = custom;
        return this;
    }
}
