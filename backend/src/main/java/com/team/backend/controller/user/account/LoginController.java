package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping(value ="/v1/user/account/login/",consumes="application/json")
    public Result login(@RequestBody LoginUser user){
        return loginService.login(user.getStudentNo(),user.getPassword());
    }
}
