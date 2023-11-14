package com.team.backend.service.report.comment;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.WeeklyReportCommentAddType;

public interface AddReportCommentService {
    Result addReportComment(WeeklyReportCommentAddType commentAddInfo);
}
