package com.team.backend.controller.equipment.record;


import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.record.CancelApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CancelApplyController {
    @Autowired
    CancelApplyService cancelApplyService;
    @PostMapping("/v1/equipment/record/cencelrecord/")
    public Result cancelApplyController(@RequestParam Map<String,String>map){
        int recordId = Integer.parseInt(map.get("recordid"));
        return cancelApplyService.cancelApply(recordId);
    }
}
