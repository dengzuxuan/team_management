package com.team.backend.controller.equipment.record;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.record.RecoverApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RecoverApplyController {
    @Autowired
    RecoverApplyService recoverApplyService;

    @PostMapping("/v1/equipment/record/recoverapplyrecord/")
    Result recoverApply(@RequestParam Map<String,String>map){
        int equipemntId = Integer.parseInt(map.get("equipemntid"));
        return recoverApplyService.recoverApply(equipemntId);
    }
}
