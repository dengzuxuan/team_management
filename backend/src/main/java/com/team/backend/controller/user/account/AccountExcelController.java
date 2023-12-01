package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.service.impl.user.account.ExportExcelServiceImpl;
import com.team.backend.service.impl.user.account.RegisterExcelServiceImpl;
import com.team.backend.utils.common.excelType.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class AccountExcelController {
    @Autowired
    RegisterExcelServiceImpl registerExcelService;
    @Autowired
    ExportExcelServiceImpl exportExcelService;

    @PostMapping("/v1/user/account/excelregister/")
    public Result registerExcel(@RequestParam("file") MultipartFile file)  {

        return registerExcelService.registerExcel(file);
    }

    @GetMapping("/v1/user/account/exportexcel/")
    public Result exportExcel()  {

        try {
            return exportExcelService.exportExcel();
        } catch (IOException e) {
            return Result.build(e.getMessage(), ResultCodeEnum.USER_EXPORY_WRONG);
        }
    }
}
