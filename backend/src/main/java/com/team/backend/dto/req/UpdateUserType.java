package com.team.backend.dto.req;

import lombok.*;

/**
 * @ClassName UpdateUserType
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:11
 * @Version 1.0
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserType {
        private String photo;
        private String email;
        private String phone;
        private String username;
}
