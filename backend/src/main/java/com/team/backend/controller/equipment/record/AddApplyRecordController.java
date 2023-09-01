package com.team.backend.controller.equipment.record;

import com.team.backend.config.result.Result;
import com.team.backend.service.equipment.record.AddApplyRecordService;
import com.team.backend.utils.common.RecordType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddApplyRecordController {
    @Autowired
    AddApplyRecordService addApplyRecordService;

    @PostMapping(value = "/v1/equipment/record/addapplyrecord/",consumes="application/json")
    public Result addApplyRecord(@RequestBody RecordType recordInfo){
        return addApplyRecordService.addApplyRecord(recordInfo);
    }
}
