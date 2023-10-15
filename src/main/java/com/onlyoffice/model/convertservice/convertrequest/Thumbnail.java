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
    private int aspect;
    private boolean first;
    private int height;
    private int width;
}
