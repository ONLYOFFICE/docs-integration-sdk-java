package core.security.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class OnlyofficeDocumentServerCredentials {
    @Builder.Default
    private String secret = "secret";
    @Builder.Default
    private String header = "Authorization";
    @Builder.Default
    private String prefix = "Bearer";
}
