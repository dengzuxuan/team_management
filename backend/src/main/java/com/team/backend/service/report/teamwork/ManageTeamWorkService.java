package com.team.backend.service.report.teamwork;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.ChangeWeeklyReportType;
import com.team.backend.dto.req.WeeklyGetReportType;
import com.team.backend.dto.resp.WeeklyReportType;

public interface ManageTeamWorkService {
    void addTeamWork(ChangeWeeklyReportType reportInfo);
    void updateTeamWork(ChangeWeeklyReportType reportInfo);
    Result getTeamWork(WeeklyGetReportType getReportInfo, int pageNum, int pageSize);
}
