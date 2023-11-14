package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.dto.req.UpdateUserType;
import com.team.backend.service.user.account.UpdateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateInfoController {
    @Autowired
    UpdateInfoService updateInfoService;

    @PostMapping(value = "/v1/user/account/updateinfo/",consumes="application/json")
    public Result updateInfo(@RequestBody UpdateUserType user){
        return updateInfoService.updateInfo(user.getEmail(),user.getPhoto(),user.getPhoto(),user.getUsername());
    }
}
