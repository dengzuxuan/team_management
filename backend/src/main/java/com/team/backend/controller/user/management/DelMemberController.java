package com.team.backend.controller.user.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.user.management.DelMemberImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DelMemberController {
    @Autowired
    DelMemberImpl delTeamMember;

    @PostMapping("/v1/user/management/delmember/")
    public Result delTeamMember(@RequestParam Map<String,String> map){
        String studentNo = map.get("studentNo");
        return delTeamMember.delTeamMember(studentNo);
    }
}
