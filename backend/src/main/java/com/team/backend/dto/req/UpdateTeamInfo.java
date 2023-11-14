package com.team.backend.dto.req;

import lombok.*;

/**
 * @ClassName UpdateTeamInfo
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:05
 * @Version 1.0
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTeamInfo {
    private String id;
    private String teamname;
}