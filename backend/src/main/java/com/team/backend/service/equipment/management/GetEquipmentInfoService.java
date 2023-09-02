package com.team.backend.service.equipment.management;

import com.team.backend.config.result.Result;

public interface GetEquipmentInfoService {
    Result getEquipmentInfo(int equipmentId);

    void updateEquipmentAndRecord(int equipmentId);
}
