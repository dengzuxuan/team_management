package com.team.backend.service.equipment.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.excel.equipmentType;

public interface UpdateEquipmentService {
    Result updateEquipment(equipmentType equipmentInfo);
}
