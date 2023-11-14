package com.team.backend.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UpdateReportType
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:51
 * @Version 1.0
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReportType implements Serializable{
        private Integer id;
        private WeekProgressType weekProgress;
        private WeekPlanType weekPlan;
        private List<TeamWorks> teamWorks;
}
