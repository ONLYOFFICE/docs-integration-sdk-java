package com.onlyoffice.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines the changes from the history object returned after saving the document.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Changes {
    /**
     * Defines the document version creation date.
     */
    private String created;

    /**
     * Defines the user who is the author of the document version.
     */
    private User user;
}
