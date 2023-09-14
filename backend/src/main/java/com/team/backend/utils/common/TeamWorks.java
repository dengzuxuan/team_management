package com.team.backend.utils.common;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TeamWorks implements Serializable {
    private String title;
    private String content;
    private int duration;
    private String id;
    private String type;
    private List<Attach> attach;
}

