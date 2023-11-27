package com.team.backend.dto.resp;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.backend.pojo.User;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ReportCommentType{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer ReportId;
    private User userInfo;
    private Integer role;
    private String content;
    private Integer isMyself;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
}