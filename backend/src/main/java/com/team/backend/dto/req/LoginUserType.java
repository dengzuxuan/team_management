package com.team.backend.dto.req;

import lombok.*;

/**
 * @ClassName LoginUserType
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:07
 * @Version 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserType {
    private String studentNo;
    private String password;

}