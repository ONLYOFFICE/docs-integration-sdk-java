package com.onlyoffice.model.convertservice.convertrequest;

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
public class Thumbnail {
    private Integer aspect;
    private boolean first;
    private Integer height;
    private Integer width;
}
