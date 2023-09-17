package com.team.backend.controller.report.info;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.info.GetUserInfosServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetUserInfosController {
    @Autowired
    GetUserInfosServiceImpl getUserInfosService;

    @GetMapping("/v1/report/info/getuserinfos/")
    Result getUserInfos(){
         return getUserInfosService.getUserInfos();
     }
}
