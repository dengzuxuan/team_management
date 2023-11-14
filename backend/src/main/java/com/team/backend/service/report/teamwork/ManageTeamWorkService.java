package com.team.backend.service.report.teamwork;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.WeeklyGetReportType;
import com.team.backend.dto.req.WeeklyReportType;

public interface ManageTeamWorkService {
    void addTeamWork(WeeklyReportType reportInfo);
    void updateTeamWork(WeeklyReportType reportInfo);
    Result getTeamWork(WeeklyGetReportType getReportInfo, int pageNum, int pageSize);
}
