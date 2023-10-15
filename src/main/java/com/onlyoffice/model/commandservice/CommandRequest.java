package com.onlyoffice.model.commandservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.onlyoffice.model.commandservice.commandrequest.Command;
import com.onlyoffice.model.commandservice.commandrequest.Meta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandRequest {
    private Command c;
    private String key;
    private List<String> users;
    private String userdata;
    private Meta meta;
}
