package com.onlyoffice.model.commandservice.commandrequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the new meta information of the document.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Meta {
    /**
     * Defines the new document name.
     */
    private String title;
}
