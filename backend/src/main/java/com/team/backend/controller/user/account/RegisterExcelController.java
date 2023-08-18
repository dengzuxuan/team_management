package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.user.account.RegisterExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.team.backend.utils.OssUtil.uploadOss;

@RestController
public class RegisterExcelController {
    @Autowired
    RegisterExcelServiceImpl registerExcelService;

    @PostMapping("/v1/user/account/excelregister/")
    public Result registerExcel(@RequestParam("file") MultipartFile file)  {

        return registerExcelService.registerExcel(file);
    }
}
