package com.team.backend.service.report.teamwork;

import com.team.backend.config.result.Result;
import com.team.backend.pojo.ReportTeamWork;
import com.team.backend.utils.common.TeamWorks;
import com.team.backend.utils.common.WeeklyGetReportType;
import com.team.backend.utils.common.WeeklyReportType;

import java.util.List;

public interface ManageTeamWorkService {
    void addTeamWork(WeeklyReportType reportInfo);
    Result getTeamWork(WeeklyGetReportType getReportInfo, int pageNum, int pageSize);
}
