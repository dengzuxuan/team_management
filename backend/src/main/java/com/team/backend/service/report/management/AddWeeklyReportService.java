package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.utils.common.WeeklyReportType;

public interface AddWeeklyReportService {
    Result addWeeklyReport(WeeklyReportType reportInfo);
}
