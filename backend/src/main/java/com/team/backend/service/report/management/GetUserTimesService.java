package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.utils.common.WeeklyGetReportType;

public interface GetUserTimesService {
    Result getUserTimes(WeeklyGetReportType getReportInfo, int pageNum, int pageSize);
}
