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
public class WeeklyReportType implements Serializable {
    private Integer id;
    private String time;
    private String year;
    private String month;
    private String week;
    private WeekProgressType weekProgress;
    private WeekPlanType weekPlan;
    private List<TeamWorks> teamWorks;
    private Integer adminStatus;
    private Integer leaderStatus;
    private Integer adminComment;
    private Integer leaderComment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}

