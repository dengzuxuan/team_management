package com.team.backend.utils.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Attach implements Serializable {
    private String fileName;
    private String url;
}
