package core.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import core.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangesHistory {
    private String created;
    private User user;
}
