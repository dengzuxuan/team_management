package com.team.backend.service.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.utils.common.equipmentType;

public interface AddEquipmentService {
    Result addEquipment(equipmentType equipmentInfo);

    ResultCodeEnum checkEquipment(equipmentType equipmentInfo);
}