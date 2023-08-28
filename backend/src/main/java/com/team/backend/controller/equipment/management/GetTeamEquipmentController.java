package com.team.backend.controller.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.management.GetTeamEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.fasterxml.jackson.core.io.NumberInput.parseInt;

@RestController
public class GetTeamEquipmentController {
    @Autowired
    GetTeamEquipmentService getTeamEquipmentService;

    @GetMapping("/v1/equipment/management/getteameuipments/")
    public Result getTeamEquipments(@RequestParam Map<String,String> map){
        int pageNum = parseInt(map.get("pageNum")) ;
        int pageSize = parseInt(map.get("pageSize"));
        return getTeamEquipmentService.getTeamEquipment(pageNum,pageSize);
    }
}
