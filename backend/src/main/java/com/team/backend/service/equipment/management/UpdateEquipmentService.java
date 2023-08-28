package com.team.backend.service.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.utils.common.equipmentType;

public interface UpdateEquipmentService {
    Result updateEquipment(equipmentType equipmentInfo);
}
