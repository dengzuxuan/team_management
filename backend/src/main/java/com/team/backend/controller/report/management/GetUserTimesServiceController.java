package com.team.backend.controller.report.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.management.GetUserTimesServiceImpl;
import com.team.backend.utils.common.WeeklyGetWorkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GetUserTimesServiceController {
    @Autowired
    GetUserTimesServiceImpl getUserTimeReport;

    @PostMapping(value = "/v1/report/management/getusertimes/",consumes="application/json")
    public Result getUserTimes(@RequestBody WeeklyGetWorkType getReportInfo, @RequestParam Map<String,String> m1){
        int pageNum = Integer.parseInt(m1.get("pageNum"));
        int pageSize = Integer.parseInt(m1.get("pageSize"));
        return getUserTimeReport.getUserTimes(getReportInfo,pageNum,pageSize);
    }
}
