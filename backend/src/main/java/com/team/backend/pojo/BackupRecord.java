package com.team.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackupRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer studentId;
    private String version;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}
