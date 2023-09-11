package com.team.backend.utils.common;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TeamWorks implements Serializable {

    private String content;
    private int duration;
    private String id;
    private String type;
}
