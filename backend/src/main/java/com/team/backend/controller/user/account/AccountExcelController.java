package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.user.account.RegisterExcelServiceImpl;
import com.team.backend.utils.common.excelType.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AccountExcelController {
    @Autowired
    RegisterExcelServiceImpl registerExcelService;

    @PostMapping("/v1/user/account/excelregister/")
    public Result registerExcel(@RequestParam("file") MultipartFile file)  {

        return registerExcelService.registerExcel(file);
    }

    @PostMapping("/v1/user/account/excelexport/")
    public Result exportExcel(@RequestBody UserType[] users)  {

        return null;
    }
}
