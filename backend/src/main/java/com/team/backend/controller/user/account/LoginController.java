package com.team.backend.controller.user.account;

import com.team.backend.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/v1/user/account/login/")
    public Map<String,String>login(@RequestParam Map<String,String> map){
        String studentNo = map.get("student_no");
        String password = map.get("password");
        return loginService.login(studentNo,password);
    }
}
