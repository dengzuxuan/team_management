package com.team.backend.controller.user.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.management.GetNoneListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetNoneListController {
    @Autowired
    GetNoneListService getNoneListService;

    @GetMapping("/v1/user/management/getnoneuserlist/")
    public Result getNoneUserList(){
        return getNoneListService.getNoneList();
    }
}
