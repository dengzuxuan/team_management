package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.dto.req.RegisterUserType;
import com.team.backend.service.user.account.RegisterService;
import com.team.backend.utils.MySQLDatabaseBackupUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;
    @PostMapping(value="/v1/user/account/register/",consumes="application/json")
    public Result register(@RequestBody RegisterUserType user){

        //MySQLDatabaseBackupUtils.backupMySQL("user");
        Result res =  registerService.register(user.getStudentNo(),user.getPassword(),user.getRole(),user.getUsername());
        return res;
    }

    @PostMapping(value="/v1/user/account/registermore/",consumes="application/json")
    public Result registerMore(@RequestBody RegisterUserType[] users){
        for (RegisterUserType user:users) {
            registerService.register(user.getStudentNo(),user.getPassword(),0,user.getUsername());
        }
        return Result.success(null);
    }
}
