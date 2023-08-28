package com.team.backend.service.equipment.management;

import com.team.backend.config.result.Result;
import org.springframework.web.multipart.MultipartFile;

public interface AddEquipmentExcelService {
    Result addEquipmentExcel(MultipartFile file);
}
