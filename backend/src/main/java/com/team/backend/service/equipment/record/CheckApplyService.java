package com.team.backend.service.equipment.record;

import com.team.backend.config.result.Result;

public interface CheckApplyService {
    Result checkApplySerive(int applyid,String status,String refuseReason);
}
