package com.team.backend.dto.resp;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserTimesInfo{
    private int id;
    private String studentNo;
    private int Role;
    private String name;
    private String teamNo;
    private String teamName;
    private int times;
}