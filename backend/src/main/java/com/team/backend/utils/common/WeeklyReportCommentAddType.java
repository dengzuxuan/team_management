package com.team.backend.utils.common;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyReportCommentAddType {
    int reportId;
    String content;
    int roleGroup;//1是回复管理员 2是回复组长
}
