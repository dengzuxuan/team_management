package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.utils.common.excelType.UserType;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName ExportExcelService
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/28 22:00
 * @Version 1.0
 */
public interface ExportExcelService {
    Result exportExcel() throws IOException;
}
