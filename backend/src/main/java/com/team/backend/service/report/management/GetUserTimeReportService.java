package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.utils.common.WeeklyGetReportType;

public interface GetUserTimeReportService {
    Result getUserTimeReport(WeeklyGetReportType getReportInfo,int pageNum,int pageSize);
}
