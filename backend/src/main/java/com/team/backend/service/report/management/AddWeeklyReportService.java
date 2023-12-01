package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.ChangeWeeklyReportType;
import com.team.backend.dto.resp.WeeklyReportType;

public interface AddWeeklyReportService {
    Result addWeeklyReport(ChangeWeeklyReportType reportInfo);
}
