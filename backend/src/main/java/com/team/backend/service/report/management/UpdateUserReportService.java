package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.UpdateReportType;

/**
 * @ClassName UpdateUserReportService
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:35
 * @Version 1.0
 */
public interface UpdateUserReportService {
    Result updateUserReportService(UpdateReportType reportInfo) ;
}
