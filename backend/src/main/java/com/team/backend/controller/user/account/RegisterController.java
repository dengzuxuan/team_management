package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    public static class RegisterUser {
        private String studentNo;
        private String password;
        private int role;
        public int getRole() {
            return role;
        }
        public String getStudentNo() {
            return studentNo;
        }
        public String getPassword() {
            return password;
        }

    }
    @Autowired
    RegisterService registerService;
    @PostMapping(value="/v1/user/account/register/",consumes="application/json")
    public Result register(@RequestBody RegisterUser user){
        return registerService.register(user.getStudentNo(),user.getPassword(),user.getRole());
    }
}