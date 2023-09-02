package com.team.backend.service.impl.equipment.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.service.equipment.record.CancelApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelApplyServiceImpl implements CancelApplyService {
    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;
    @Override
    public Result cancelApply(int applyId) {
        QueryWrapper<EquipmentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",applyId);
        EquipmentRecord equipmentRecord = equipmentRecordMapper.selectOne(queryWrapper);
        if(equipmentRecord==null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_ERCORD_NOT_EXIST);
        }

        UpdateWrapper<EquipmentRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",applyId);
        updateWrapper.set("status",6);
        equipmentRecordMapper.update(null,updateWrapper);

        return Result.success(null);
    }
}

