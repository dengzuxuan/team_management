package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.service.backup.ManagementAppendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadAppendController {
    @Autowired
    ManagementAppendService managementAppendService;

    @PostMapping("v1/user/account/uploadphoto/")
    public Result upload(@RequestParam("file") MultipartFile file) {
        return managementAppendService.addAppend(file);
    }
}
