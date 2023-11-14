package com.team.backend.controller.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.report.management.AddWeeklyReportService;
import com.team.backend.dto.req.WeeklyReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddWeeklyReportController {
    @Autowired
    AddWeeklyReportService addWeeklyReportService;

    @PostMapping(value = "/v1/report/management/addweeklyreport/",consumes="application/json")
    public Result addWeeklyReport(@RequestBody WeeklyReportType reportInfo){
        return addWeeklyReportService.addWeeklyReport(reportInfo);
    }
}
