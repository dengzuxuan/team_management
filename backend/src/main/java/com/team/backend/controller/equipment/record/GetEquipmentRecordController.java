package com.team.backend.controller.equipment.record;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.equipment.record.GetEquipmentRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetEquipmentRecordController {
    @Autowired
    GetEquipmentRecordServiceImpl getEquipmentRecordService;
    @GetMapping("/v1/equipment/record/getequipmentrecord/")
    public Result checkApply(@RequestParam Map<String,String> map){
        int equipmentid = Integer.parseInt(map.get("equipmentid"));
        return getEquipmentRecordService.getEquipmentRecord(equipmentid);
    }
}
