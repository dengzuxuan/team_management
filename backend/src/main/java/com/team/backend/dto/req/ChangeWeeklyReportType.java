package com.team.backend.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeWeeklyReportType implements Serializable {
    private Integer id;
    private Boolean createFlag;
    private String time;
    private String year;
    private String month;
    private String week;
    private WeekProgressType weekProgress;
    private WeekPlanType weekPlan;
    private List<TeamWorks> teamWorks;
}

