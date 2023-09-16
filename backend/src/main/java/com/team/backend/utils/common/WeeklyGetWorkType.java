package com.team.backend.utils.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyGetWorkType implements Serializable {
    String studentNo;
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

