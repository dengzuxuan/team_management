package com.team.backend.controller.user.account;


import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.UpdateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateInfoController {
    public static class UpdateUser {

        public String getPhoto() {
            return photo;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        private String photo;
        private String email;
        private String phone;


    }
    @Autowired
    UpdateInfoService updateInfoService;

    @PostMapping(value = "/v1/user/account/updateinfo/",consumes="application/json")
    public Result updateInfo(@RequestBody UpdateUser user){
        return updateInfoService.updateInfo(user.email,user.photo,user.phone);
    }
}
