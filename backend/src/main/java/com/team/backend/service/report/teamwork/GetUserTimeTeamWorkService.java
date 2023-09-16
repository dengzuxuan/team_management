package com.team.backend.service.report.teamwork;

import com.team.backend.config.result.Result;
import com.team.backend.utils.common.WeeklyGetWorkType;

public interface GetUserTimeTeamWorkService {
    Result getUserTimeTeamWork(WeeklyGetWorkType getReportInfo, int pageNum, int pageSize);
}
