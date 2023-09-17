package com.team.backend.service.report.comment;

import com.team.backend.config.result.Result;
import com.team.backend.utils.common.WeeklyReportCommentAddType;

public interface AddReportCommentService {
    Result addReportComment(WeeklyReportCommentAddType commentAddInfo);
}
