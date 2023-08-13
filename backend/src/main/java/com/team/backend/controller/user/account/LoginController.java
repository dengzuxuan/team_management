package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    public static class LoginUser {
        private String studentNo;
        private String password;
        public String getStudentNo() {
            return studentNo;
        }
        public String getPassword() {
            return password;
        }

    }
    @Autowired
    private LoginService loginService;

    @PostMapping(value ="/v1/user/account/login/",consumes="application/json")
    public Result login(@RequestBody LoginUser user){
        return loginService.login(user.getStudentNo(),user.getPassword());
    }
}
