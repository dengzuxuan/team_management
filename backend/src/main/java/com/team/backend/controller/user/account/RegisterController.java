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
        private String username;

        public RegisterUser() {
        }

        public int getRole() {
            return role;
        }
        public String getStudentNo() {
            return studentNo;
        }
        public String getPassword() {
            return password;
        }
        public String getUsername() {
            return username;
        }

    }
    @Autowired
    RegisterService registerService;
    @PostMapping(value="/v1/user/account/register/",consumes="application/json")
    public Result register(@RequestBody RegisterUser user){
        return registerService.register(user.getStudentNo(),user.getPassword(),user.getRole(),user.getUsername());
    }

    @PostMapping(value="/v1/user/account/registermore/",consumes="application/json")
    public Result registerMore(@RequestBody RegisterUser[] users){
        for (RegisterUser user:users) {
            registerService.register(user.getStudentNo(),user.getPassword(),0,user.getUsername());
        }
        return Result.success(null);
    }
}
