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
import com.team.backend.service.equipment.record.CheckApplySerive;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckApplySeriveImpl implements CheckApplySerive {
    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;

    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public Result checkApplySerive(int applyid,String status,String refuseReason) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=2 && user.getRole()!=3){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        EquipmentRecord equipmentRecord = equipmentRecordMapper.selectById(applyid);
        if(equipmentRecord==null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_ERCORD_NOT_EXIST);
        }

        if("pass".equals(status)){
            //本条记录状态修订
            UpdateWrapper<EquipmentRecord> equipmentRecordUpdateWrapper = new UpdateWrapper<>();
            equipmentRecordUpdateWrapper.eq("id",applyid);
            equipmentRecordUpdateWrapper.set("status",3).set("check_no",user.getStudentNo());
            equipmentRecordMapper.update(null,equipmentRecordUpdateWrapper);

            //申请同一设备的记录状态修订
            QueryWrapper<EquipmentRecord> equipmentRecordQueryWrapper = new QueryWrapper<>();
            equipmentRecordQueryWrapper.eq("equipment_id",equipmentRecord.getEquipmentId()).eq("status",2);
            List<EquipmentRecord> equipmentRecordList = equipmentRecordMapper.selectList(equipmentRecordQueryWrapper);

            for (EquipmentRecord record:equipmentRecordList) {
                UpdateWrapper<EquipmentRecord> equipmentRecordUpdateWrapper1 = new UpdateWrapper<>();
                equipmentRecordUpdateWrapper1.eq("id",record.getId());
                equipmentRecordUpdateWrapper1.set("status",5).set("refuse_reason","该设备已被他人分配").set("check_no",user.getStudentNo());
                equipmentRecordMapper.update(null,equipmentRecordUpdateWrapper1);
            }

            //设备状态记录修订
            UpdateWrapper<Equipment> equipmentUpdateWrapper = new UpdateWrapper<>();
            equipmentUpdateWrapper.eq("id",equipmentRecord.getEquipmentId());
            equipmentUpdateWrapper.set("status",2);
            equipmentMapper.update(null,equipmentUpdateWrapper);

        }else if("refuse".equals(status)){
            if("".equals(refuseReason)){
                refuseReason = "未给出拒绝原因";
            }

            UpdateWrapper<EquipmentRecord> equipmentRecordUpdateWrapper = new UpdateWrapper<>();
            equipmentRecordUpdateWrapper.eq("id",applyid);
            equipmentRecordUpdateWrapper.set("status",5).set("check_no",user.getStudentNo()).set("refuse_reason",refuseReason);
            equipmentRecordMapper.update(null,equipmentRecordUpdateWrapper);
        }else{
            return Result.build(null,ResultCodeEnum.INPUT_PARAM_WRONG);
        }

        return Result.success(null);
    }
}
