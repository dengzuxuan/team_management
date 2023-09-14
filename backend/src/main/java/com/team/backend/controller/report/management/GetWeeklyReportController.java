package com.team.backend.controller.report.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.management.GetWeeklyReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetWeeklyReportController {
    @Autowired
    GetWeeklyReportServiceImpl getWeeklyReportService;

    @GetMapping("/v1/report/management/getweeklyreport/")
    public Result getWeeklyRecord(@RequestParam Map<String,String>m1){
        int pageNum = Integer.parseInt(m1.get("pageNum"));
        int pageSize = Integer.parseInt(m1.get("pageSize"));
        return getWeeklyReportService.getWeeklyReport(pageNum,pageSize);
    }
}
