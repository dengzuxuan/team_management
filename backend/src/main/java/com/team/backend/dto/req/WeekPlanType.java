package com.team.backend.dto.req;

import com.team.backend.dto.req.Attach;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekPlanType implements Serializable {
    private String content;
    private List<Attach> attach;
}
