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
public class Equipment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String serialNumber;
    private String name;
    private String version;
    private String originalValue;
    private String performanceIndex;
    private String address;
    private String warehouseEntrytime;
    private String hostRemarks;
    private String remark;
    private Integer status;
    private String adminNo;
    private String creator;
    private String recipient;
    private String formerRecipient;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}
