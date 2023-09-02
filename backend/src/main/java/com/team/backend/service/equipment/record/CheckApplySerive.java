package com.team.backend.service.equipment.record;

import com.team.backend.config.result.Result;

public interface CheckApplySerive {
    Result checkApplySerive(int applyid,String status,String refuseReason);
}
