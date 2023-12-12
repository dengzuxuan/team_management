package com.team.backend.dto.req;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePassword {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}