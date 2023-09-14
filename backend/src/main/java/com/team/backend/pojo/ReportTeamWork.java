package com.team.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTeamWork {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer reportId;
    private String year;
    private String week;
    private String studentNo;
    private String content;
    private int duration;
    private String teamworkId;
    private String type;
    private String title;
    private String attach;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}
