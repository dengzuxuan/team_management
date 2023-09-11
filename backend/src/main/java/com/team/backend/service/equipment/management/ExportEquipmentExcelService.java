package com.team.backend.service.equipment.management;

import com.team.backend.config.result.Result;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ExportEquipmentExcelService {
    Result exportEquipmentExcel(String[] fields) throws IOException;
}
