package com.team.backend.dto.req;

import com.team.backend.dto.req.Attach;
import lombok.*;

import java.io.Serializable;
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

