package com.team.backend.controller.user.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.management.UpdateRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class UpdateUserRoleController {
    @Autowired
    UpdateRoleService updateRoleService;
    @PostMapping ("/v1/user/management/updaterole/")
    public Result updateRole(@RequestParam Map<String,String> map){
        String role = map.get("role");
        String studentNo = map.get("studentNo");
        return updateRoleService.updateRole(studentNo,role);
    }
}
