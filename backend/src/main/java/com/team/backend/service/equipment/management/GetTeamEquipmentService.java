package com.team.backend.service.equipment.management;

import com.team.backend.config.result.Result;

public interface GetTeamEquipmentService {
    Result getTeamEquipment(int pageNum,int pageSize);
}
