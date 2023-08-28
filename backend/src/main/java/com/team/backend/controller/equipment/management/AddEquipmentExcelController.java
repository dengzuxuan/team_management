package com.team.backend.controller.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.management.AddEquipmentExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AddEquipmentExcelController {

    @Autowired
    AddEquipmentExcelService addEquipmentExcelService;

    @PostMapping("/v1/equipment/management/addequipmentexcel/")
    public Result addEquipmentExcel(@RequestParam("file") MultipartFile file)  {
        return addEquipmentExcelService.addEquipmentExcel(file);
    }
}
