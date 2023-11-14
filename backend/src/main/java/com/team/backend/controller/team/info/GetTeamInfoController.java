package com.team.backend.controller.team.info;

import com.team.backend.config.result.Result;
import com.team.backend.service.team.info.GetTeamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetTeamInfoController {

    @Autowired
    GetTeamInfoService getTeamInfoService;

//    public Result getTeamInfo(@RequestParam("studentNo")  String StudentNo){
//        return getTeamInfoService.getTeamInfo(StudentNo);
//    }
    @GetMapping(value = "/v1/team/info/getinfos/")
    public Result getTeamInfo(@RequestParam Map<String,String> map){
        String StudentNo = map.get("studentNo");
        return getTeamInfoService.getTeamInfo(StudentNo);
    }
}
