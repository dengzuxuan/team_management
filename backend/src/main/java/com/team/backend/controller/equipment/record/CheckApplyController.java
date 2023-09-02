package com.team.backend.controller.equipment.record;


import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.record.CheckApplySerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CheckApplyController {
    @Autowired
    CheckApplySerive checkApplySerive;

    @PostMapping("/v1/equipment/record/checkrecord/")
    public Result checkApply(@RequestParam Map<String,String>map){
        int applyId = Integer.parseInt(map.get("applyid"));
        String status = map.get("status");
        String reason = map.get("reason");
        return checkApplySerive.checkApplySerive(applyId,status,reason);
    }
}
