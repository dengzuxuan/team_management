package com.team.backend.dto.resp;

import com.team.backend.pojo.ReportTeamWork;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamWorkInfo{
    private int id;
    private String studentNo;
    private int Role;
    private String name;
    private String teamNo;
    private String teamName;
    private int workTimes;
    private int workTotalDuration;
    private List<ReportTeamWork> teamWorksList;
}