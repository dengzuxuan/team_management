package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.dto.req.AdminUpdatePassword;
import com.team.backend.dto.req.UpdatePassword;
import com.team.backend.service.user.account.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdatePasswordController {

    @Autowired
    UpdatePasswordService updatePasswordService;

    @PostMapping(value = "/v1/user/account/updatepassword/",consumes="application/json")
    public Result updatePassword(@RequestBody UpdatePassword updateInfo){
        return updatePasswordService.updatePassword(
                updateInfo.getOldPassword(),
                updateInfo.getNewPassword(),
                updateInfo.getConfirmPassword()
        );
    }

    @PostMapping(value = "/v1/user/account/adminupdatepassword/")
    public Result adminUpdatePassword(@RequestParam Map<String,String> map){
        String studentNo = map.get("studentNo");
        return updatePasswordService.adminUpdatePassword(
                studentNo
        );
    }

}
