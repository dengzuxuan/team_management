package com.team.backend.utils.common;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyReportType implements Serializable {
    private String time;
    private String year;
    private String month;
    private String weekPlanHtml;
    private String nextWeekHtml;
    private List<TeamWorks> teamWorks;
}
