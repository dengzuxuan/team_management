package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.WeeklyGetWorkType;

public interface GetUserTimesService {
    Result getUserTimes(WeeklyGetWorkType getReportInfo, int pageNum, int pageSize);
}
