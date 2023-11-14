package com.team.backend.service.equipment.record;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.RecordType;

public interface AddApplyRecordService {
    Result addApplyRecord(RecordType recordInfo);
}
