package com.team.backend.dto.req;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class MemberChangeType implements Serializable {
    String originStudentNo;
    String newStudentNo;
    String newName;
}
