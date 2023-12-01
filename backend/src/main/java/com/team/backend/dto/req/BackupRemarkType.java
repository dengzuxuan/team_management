package com.team.backend.dto.req;

import lombok.*;

import java.io.Serializable;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/1
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackupRemarkType implements Serializable {
    private String remark;
}
