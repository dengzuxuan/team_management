package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.dto.req.RegisterUserType;
import com.team.backend.service.user.account.RegisterService;
import com.team.backend.utils.MySQLDatabaseBackupUtils;
import com.team.backend.utils.common.excelType.UserType;
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
    public Result register(@RequestBody UserType user){

        //MySQLDatabaseBackupUtils.backupMySQL("user");
        return registerService.register(user);
    }

    @PostMapping(value="/v1/user/account/registermore/",consumes="application/json")
    public Result registerMore(@RequestBody UserType[] users){
        return registerService.registermore(users);
    }
}
