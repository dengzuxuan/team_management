package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.user.account.GetSingleInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetSingleInfoController {
    @Autowired
    GetSingleInfoServiceImpl getSingleInfoService;
    @GetMapping("/v1/user/account/getsingleinfo/")
    public Result getSingleInfo(@RequestParam Map<String,String> map){
        String studentNo = map.get("studentNo");
        return getSingleInfoService.getSingleInfo(studentNo);
    }
}
