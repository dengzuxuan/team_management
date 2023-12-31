package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.GetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetInfoController {
    @Autowired
    private GetInfoService infoService;

    @GetMapping(value ="/v1/user/account/info/")
    public Result info(){
        return infoService.getInfo();
    }
}
