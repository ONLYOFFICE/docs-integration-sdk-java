package core.model.method;

import com.fasterxml.jackson.annotation.JsonInclude;
import core.model.common.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SetUsers {
    private List<User> users = new ArrayList<>();
}
