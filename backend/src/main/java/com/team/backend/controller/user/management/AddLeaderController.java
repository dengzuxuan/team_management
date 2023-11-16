package com.team.backend.controller.user.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.team.management.AddTeamService;
import com.team.backend.service.user.management.UpdateRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class AddLeaderController {
    @Autowired
    UpdateRoleService updateRoleService;
    @Autowired
    AddTeamService addTeamService;
    @PostMapping ("/v1/user/management/addleader/")
    public Result updateRole(@RequestParam Map<String,String> map){
        String studentNo = map.get("studentNo");
        //用户表变更
        Result res =  updateRoleService.updateRole(studentNo,"2");

        //小组表变更
//        if (res.getCode()==200){
//            res = addTeamService.addTeamService(studentNo);
//        }

        return res;
    }
}
