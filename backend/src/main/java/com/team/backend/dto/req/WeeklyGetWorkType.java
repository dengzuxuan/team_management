package com.team.backend.dto.req;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyGetWorkType implements Serializable {
    String no; //单独成员->学号 某小组下所有成员->小组编号
    timeInfo startTimeInfo;
    timeInfo endTimeInfo;
    @Getter
    @Setter
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class timeInfo implements Serializable{
        int year;
        int week;
    }
}

