package com.team.backend.controller.equipment.record;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.record.GetApplyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetApplyRecordController {
    @Autowired
    GetApplyRecordService getApplyRecordService;

    @GetMapping("/v1/equipment/record/getapplyrecord/")
    public Result getApplyRecord(){
        return getApplyRecordService.getApplyRecord();
    }
}
