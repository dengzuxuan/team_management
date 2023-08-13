package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    RegisterService registerService;
    @PostMapping("/v1/user/account/register/")
    public Result register(@RequestParam Map<String,String>map){
        String studentNo = map.get("student_no");
        return registerService.register(studentNo);
    }
}
