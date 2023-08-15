package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdatePasswordController {
    public static class updatePassword{
        public String getConfirmPassword() {
            return confirmPassword;
        }


        public String getOldPassword() {
            return oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
    }

    @Autowired
    UpdatePasswordService updatePasswordService;

    @PostMapping(value = "/v1/user/account/updatepassword/",consumes="application/json")
    public Result updatePassword(@RequestBody updatePassword updatePassword){
        return updatePasswordService.updatePassword(
                updatePassword.oldPassword,updatePassword.newPassword,updatePassword.confirmPassword);
    }
}
