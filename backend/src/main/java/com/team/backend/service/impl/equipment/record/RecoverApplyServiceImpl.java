package com.team.backend.service.impl.equipment.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.EquipmentMapper;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.pojo.Equipment;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.record.RecoverApplyService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RecoverApplyServiceImpl implements RecoverApplyService {
    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;
    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public Result recoverApply(int equipemntid) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=1){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",equipemntid);
        Equipment equipment =  equipmentMapper.selectOne(queryWrapper);
        if(equipment==null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_ERCORD_NOT_EXIST);
        }

        QueryWrapper<EquipmentRecord> equipmentRecordQueryWrapper = new QueryWrapper<>();
        equipmentRecordQueryWrapper.eq("equipment_id",equipemntid).eq("status",3);
        EquipmentRecord equipmentRecord = equipmentRecordMapper.selectOne(equipmentRecordQueryWrapper);
        if(equipmentRecord==null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_ERCORD_CANT_RECOVER);
        }

        UpdateWrapper<EquipmentRecord> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("equipment_id",equipemntid);
        updateWrapper.set("status",4);
        updateWrapper.set("refuse_reason","该设备被管理员提前收回");
        equipmentRecordMapper.update(null,updateWrapper);


        UpdateWrapper<Equipment>updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("id",equipemntid);
        updateWrapper1.set("status",1).set("former_recipient",equipmentRecord.getStudentNo()).set("recipient",null);
        equipmentMapper.update(null,updateWrapper1);

        return Result.success(null);
    }
}
