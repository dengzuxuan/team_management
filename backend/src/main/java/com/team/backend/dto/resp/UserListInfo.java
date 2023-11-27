package com.team.backend.dto.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import lombok.*;

import java.util.Date;

/**
 * @ClassName UserListInfo
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/27 19:55
 * @Version 1.0
 */
@Getter
@Setter
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserListInfo {
    private Integer id;
    private String teamNo;
    private String leaderNo;
    private String adminNo;
    private String username;
    private String phone;
    private String email;
    private int role;
    private String cardNo;
    private String studentNo;
    private User leaderInfo;
    private TeamInfo teamInfo;
}
