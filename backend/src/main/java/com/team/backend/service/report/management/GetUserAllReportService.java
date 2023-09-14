package com.team.backend.service.report.management;

import com.team.backend.config.result.Result;
import org.springframework.web.bind.annotation.GetMapping;

public interface GetUserAllReportService {
    Result getUserAllReport(String studentNo,int pageNum,int pageSize);
}
