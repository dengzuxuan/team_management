package com.team.backend.controller.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.management.GetEquipmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetEquipmentInfoController {
    @Autowired
    GetEquipmentInfoService getEquipmentInfoService;

    @GetMapping("/v1/equipment/management/geteuipmentinfo/")
    Result getEquipmentInfo(@RequestParam Map<String,String>map){
        int equipmentId = Integer.parseInt(map.get("equipmentid"));
        return getEquipmentInfoService.getEquipmentInfo(equipmentId);
    }
}
