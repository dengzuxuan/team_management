package com.team.backend.service.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;

import java.util.HashMap;
import java.util.Map;

public interface GetWeeklyReportService {
    Result getWeeklyReport(int pageNum, int pageSize);

    Map<String,Object> getReportPage(int pageNum, int pageSize, QueryWrapper queryWrapper);
}
