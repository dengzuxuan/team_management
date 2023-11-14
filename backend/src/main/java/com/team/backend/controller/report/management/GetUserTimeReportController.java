package com.team.backend.controller.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.management.GetUserTimeReportServiceImpl;
import com.team.backend.dto.req.WeeklyGetReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GetUserTimeReportController {
    @Autowired
    GetUserTimeReportServiceImpl getUserTimeReportService;

    @PostMapping(value = "/v1/report/management/getusertimereport/",consumes="application/json")
    public Result getUserTimeReport(@RequestBody WeeklyGetReportType getReportInfo,@RequestParam Map<String,String> m1){
        int pageNum = Integer.parseInt(m1.get("pageNum"));
        int pageSize = Integer.parseInt(m1.get("pageSize"));
        return getUserTimeReportService.getUserTimeReport(getReportInfo,pageNum,pageSize);
    }
}
