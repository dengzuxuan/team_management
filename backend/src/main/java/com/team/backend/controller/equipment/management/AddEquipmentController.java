package com.team.backend.controller.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.management.AddEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.team.backend.dto.excel.equipmentType;
@RestController
public class AddEquipmentController {
    @Autowired
    AddEquipmentService addEquipmentService;

    @PostMapping(value = "/v1/equipment/management/addequipment/",consumes="application/json")
    public Result addEquipment(@RequestBody equipmentType equipmentType){
        return addEquipmentService.addEquipment(equipmentType);
    }

    @PostMapping(value="/v1/equipment/management/addequipmentmore/",consumes="application/json")
    public Result registerMore(@RequestBody equipmentType[] equipmentTypes){
        for (equipmentType equipmentType:equipmentTypes) {
            addEquipmentService.addEquipment(equipmentType);
        }
        return Result.success(null);
    }
}
