package com.team.backend.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("team_info")
public class TeamInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String no;
    private String adminNo;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer leaderId;
    private String teamname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}
