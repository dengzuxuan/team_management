package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface RegisterExcelService {
    Result registerExcel(MultipartFile file);
}
