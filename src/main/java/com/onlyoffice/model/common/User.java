package com.onlyoffice.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Defines the user currently viewing or editing the document.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class User {
    /**
     * Defines the identification of the user. The length is limited to 128 symbols.
     * This information is stored and used to distinguish co-authors,
     * indicate the author of the last changes when saving and highlighting history (in the list of changes),
     * and count users with access for a license based on the number of users.
     * We recommend using some unique anonymized hash.
     * Do not use sensitive data, like name or email for this field.
     */
    private String id;

    /**
     * Defines the full name of the user. The length is limited to 128 symbols.
     * Used since version 4.2
     */
    private String name;

    /**
     * Defines the group the user belongs to.
     */
    private String group;
}
