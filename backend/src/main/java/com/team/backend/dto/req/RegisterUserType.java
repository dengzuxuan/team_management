package com.team.backend.dto.req;

import lombok.*;

/**
 * @ClassName RegisterUserType
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:08
 * @Version 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserType {
        private String username;
        private String studentNo;
        private String tel;
        private String email;
        private String cardNo;
        private String teamNo;
        private String teamName;
        private String password;
}
