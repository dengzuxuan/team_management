package com.team.backend.pojo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String teamNo;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer leaderId;
    private String adminNo;
    private String username;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String password;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String phone;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String email;
    private int role;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String cardNo;
    private String studentNo;


    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String passwordReal;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}

