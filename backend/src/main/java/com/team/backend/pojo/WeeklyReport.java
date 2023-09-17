package com.team.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyReport implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String time;
    private String year;
    private String month;
    private String week;
    private String weekProgress;
    private String weekPlan;
    private String teamworkInfos;
    private Integer teamworkDuration;
    private String studentNo;
    private Integer adminStatus;
    private Integer leaderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}
