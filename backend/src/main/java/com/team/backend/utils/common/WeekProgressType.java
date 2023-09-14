package com.team.backend.utils.common;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekProgressType implements Serializable {
    private String content;
    private List<Attach> attach;
}
