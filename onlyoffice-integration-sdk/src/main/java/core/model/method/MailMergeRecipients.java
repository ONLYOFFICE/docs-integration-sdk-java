package core.model.method;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class MailMergeRecipients {
    private final String fileType;
    private final String url;
    @Setter
    private String token;
}
