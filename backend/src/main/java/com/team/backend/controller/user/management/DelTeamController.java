package com.team.backend.controller.user.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.user.management.DelMemberImpl;
import com.team.backend.service.user.management.DelTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DelTeamController {
    @Autowired
    DelTeamService delTeamService;

    @PostMapping("/v1/user/management/delteam/")
    public Result delTeam(@RequestParam Map<String,String>m1){
        String studentNo = m1.get("studentNo");
        return delTeamService.delTeam(studentNo);
    }
}
