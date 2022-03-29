package core.model.command.license.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class License {
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    private Boolean timelimited;
    private Boolean trial;
    private Boolean developer;
    private String mode;
    private Boolean light;
    private Boolean branding;
    private Boolean customization;
    private Boolean plugins;
    private int connections;
    @JsonProperty("users_count")
    private int usersCount;
    @JsonProperty("users_expire")
    private int usersExpire;
}
