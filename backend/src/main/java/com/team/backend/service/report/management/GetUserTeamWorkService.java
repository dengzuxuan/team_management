package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.WeeklyGetReportType;

public interface GetUserTeamWorkService {
    Result getUserTeamWork(WeeklyGetReportType getReportInfo, int pageNum, int pageSize);
}
