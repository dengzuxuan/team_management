package com.team.backend.controller.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.UpdateReportType;
import com.team.backend.dto.req.WeeklyReportType;
import com.team.backend.service.report.management.UpdateUserReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UpdateUserReportController
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:34
 * @Version 1.0
 */
@RestController
public class UpdateUserReportController {
//    @Autowired
//    UpdateUserReportService updateUserReportService;
//
//    @PostMapping(value = "/v1/report/management/updateweeklyreport/",consumes="application/json")
//    public Result addWeeklyReport(@RequestBody UpdateReportType reportInfo){
//        return updateUserReportService.updateUserReportService(reportInfo);
//    }
}
