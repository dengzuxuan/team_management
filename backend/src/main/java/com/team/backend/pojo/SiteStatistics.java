package com.team.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName SiteStatistics
 * @Description TODO
 * @Author Colin
 * @Date 2023/10/20 15:56
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteStatistics {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String pageUrl;
    private Integer count;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}
