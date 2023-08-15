package com.team.backend.controller.user.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.management.AddMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddMemberController {
    @Autowired
    AddMemberService addMemberService;

    @PostMapping("/v1/user/management/addmember/")
    public Result addMember(@RequestParam Map<String,String> map){
        String leaderStudentNo = map.get("leaderStudentNo");
        String memberStudentNo = map.get("memberStudentNo");
        return addMemberService.addMember(leaderStudentNo,memberStudentNo);
    }
}
