package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

import static com.team.backend.utils.OssUtil.uploadOss;

@RestController
public class UploadPhotoController {
    @PostMapping("v1/user/account/uploadphoto/")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        String name = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        return uploadOss(file.getInputStream(),name);
    }
}
