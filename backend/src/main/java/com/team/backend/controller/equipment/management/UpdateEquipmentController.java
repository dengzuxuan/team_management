package com.team.backend.controller.equipment.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.management.UpdateEquipmentService;
import com.team.backend.dto.req.equipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateEquipmentController {
    @Autowired
    UpdateEquipmentService updateEquipmentService;

    @PostMapping(value="/v1/equipment/management/updateequipment/",consumes="application/json")
    public Result updateEquipment(@RequestBody equipmentType equipmentInfo){
        return updateEquipmentService.updateEquipment(equipmentInfo);
    }
}
