package com.onlyoffice.model.convertservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.common.RequestEntity;
import com.onlyoffice.model.convertservice.convertrequest.Thumbnail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvertRequest implements RequestEntity {
    private Boolean async;
    private int codePage;
    private int delimiter;
    private String filetype;
    private String key;
    private String outputtype;
    private String password;
    private String region;
    private Thumbnail thumbnail;
    private String title;
    private String token;
    private String url;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(final String tn) {
        this.token = tn;
    }
}
