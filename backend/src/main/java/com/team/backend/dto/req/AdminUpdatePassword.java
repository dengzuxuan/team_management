package com.team.backend.dto.req;

import lombok.*;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/12
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdatePassword {

    private String StudentNo;
    private String newPassword;
}
