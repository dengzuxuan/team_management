package com.team.backend.controller.team.info;

import com.team.backend.config.result.Result;
import com.team.backend.service.team.info.GetTeamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetTeamInfoController {

    @Autowired
    GetTeamInfoService getTeamInfoService;

    @GetMapping(value = "/v1/team/info/getinfos/")
    public Result getTeamInfo(){
        return getTeamInfoService.getTeamInfo();
    }
}