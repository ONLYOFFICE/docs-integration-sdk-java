package com.onlyoffice.model.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Credentials {
    private String key;
    private String header;
    private String prefix;
}
