package com.team.backend.controller.user.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.UpdateUserType;
import com.team.backend.service.user.management.UpdateLeaderService;
import com.team.backend.service.user.management.UpdateUserInfoService;
import com.team.backend.utils.common.excelType.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateMemberInfoController {
    @Autowired
    UpdateUserInfoService updateUserInfoService;

    @PostMapping(value = "/v1/user/management/updateuserinfo/",consumes="application/json")
    public Result UpdateLeader(@RequestBody UpdateUserType user){
        return updateUserInfoService.updateUserInfo(user);
    }
}
