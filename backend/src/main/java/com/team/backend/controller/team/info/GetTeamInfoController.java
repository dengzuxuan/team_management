package com.team.backend.controller.team.info;

import com.team.backend.config.result.Result;
import com.team.backend.service.team.info.GetTeamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.fasterxml.jackson.core.io.NumberInput.parseInt;

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

    @GetMapping(value = "/v1/team/info/getinfodetails/")
    public Result getTeamDetailInfo(){
        return getTeamInfoService.getTeamDetail();
    }

    @GetMapping(value = "/v1/team/info/getteamuserinfo/")
    public Result getTeamUserInfo(@RequestParam Map<String,String> map){
        String teamNo = map.get("no");
        return getTeamInfoService.getTeamUserInfo(teamNo);
    }

    @GetMapping(value = "/v1/team/info/getallinfos/")
    public Result getAllTeamInfo(@RequestParam Map<String,String> map){
        int pageNum = parseInt(map.get("pageNum")) ;
        int pageSize = parseInt(map.get("pageSize"));
        return getTeamInfoService.getAllTeamInfos(pageNum,pageSize);
    }
}
