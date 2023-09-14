package com.team.backend.controller.report.teamwork;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.teamwork.ManageTeamWorkServiceImpl;
import com.team.backend.utils.common.WeeklyGetReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetUserTeamWorkController {
    @Autowired
    ManageTeamWorkServiceImpl manageTeamWorkService;

    @GetMapping(value = "/v1/report/teamwork/getuserteamwork/",consumes="application/json")
    public Result getUserTeamWork(@RequestBody WeeklyGetReportType getReportInfo, @RequestParam Map<String,String> m1){
        int pageNum = Integer.parseInt(m1.get("pageNum"));
        int pageSize = Integer.parseInt(m1.get("pageSize"));
        return manageTeamWorkService.getTeamWork(getReportInfo,pageNum,pageSize);
    }
}
