package com.team.backend.controller.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.management.ExportEquipmentExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ExportEquipmentExcelController {
    @Autowired
    ExportEquipmentExcelService exportEquipmentExcelService;

    @PostMapping("/v1/equipment/management/exportexcel/")
    public Result exportEquipmentExcel(@RequestParam(value = "fields") String[] memberStudentNos) throws IOException {
        return exportEquipmentExcelService.exportEquipmentExcel(memberStudentNos);
    }
}
