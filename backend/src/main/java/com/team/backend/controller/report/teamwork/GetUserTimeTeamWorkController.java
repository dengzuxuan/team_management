package com.team.backend.controller.report.teamwork;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.teamwork.GetUserTimeTeamWorkServiceImpl;
import com.team.backend.service.report.teamwork.GetUserTimeTeamWorkService;
import com.team.backend.utils.common.WeeklyGetReportType;
import com.team.backend.utils.common.WeeklyGetWorkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GetUserTimeTeamWorkController {
    @Autowired
    GetUserTimeTeamWorkServiceImpl getUserTimeTeamWorkService;
    @PostMapping(value = "/v1/report/teamwork/getusertimeteamwork/",consumes="application/json")
    Result getUserTimeTeakWork(@RequestBody WeeklyGetWorkType getWorkInfo, @RequestParam Map<String,String> m1){
        int pageNum = Integer.parseInt(m1.get("pageNum"));
        int pageSize = Integer.parseInt(m1.get("pageSize"));
        return getUserTimeTeamWorkService.getUserTimeTeamWork(getWorkInfo,pageNum,pageSize);
    }
}
