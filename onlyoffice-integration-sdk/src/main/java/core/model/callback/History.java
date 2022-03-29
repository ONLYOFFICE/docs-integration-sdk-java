package core.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import core.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class History {
    private String serverVersion;
    private String key;
    private Integer version;
    private String created;
    private User user;
    private List<ChangesHistory> changes;

    /**
     *
     * @param created
     * @param key
     * @param version
     * @return
     */
    public History(String created, String key, int version) {
        this.created = created;
        this.key = key;
        this.version = version;
    }
}
