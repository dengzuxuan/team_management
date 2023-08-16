package com.team.backend.controller.user.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.management.GetTeamUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetTeamUsersController {
    @Autowired
    GetTeamUsersService getTeamUsersService;

    @GetMapping("/v1/user/management/getteaminfos/")
    public Result getTeamInfos(@RequestParam Map<String,String>m1){
        String studentNo = m1.get("studentNo");
        return getTeamUsersService.getTeamUsers(studentNo);
    }
}
