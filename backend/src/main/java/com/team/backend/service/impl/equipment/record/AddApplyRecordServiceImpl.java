package com.team.backend.service.impl.equipment.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.EquipmentMapper;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.pojo.Equipment;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.record.AddApplyRecordService;
import com.team.backend.service.equipment.record.CheckApplyService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.utils.common.RecordType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddApplyRecordServiceImpl implements AddApplyRecordService {
    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;

    @Autowired
    CheckApplyService checkApplySerive;

    @Override
    public Result addApplyRecord(RecordType record) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<Equipment> equipmentQueryWrapper = new QueryWrapper<>();
        equipmentQueryWrapper.eq("id",record.getEquipmentId());
        Equipment equipment = equipmentMapper.selectOne(equipmentQueryWrapper);

        if(equipment==null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_NOT_EXIST);
        }

        String reason = record.getApplyReason();
        if("".equals(reason)){
            reason = "无原因";
        }

        QueryWrapper<EquipmentRecord> equipmentRecordQueryWrapper = new QueryWrapper<>();
        //正在申请的设备无法重复申请
        equipmentRecordQueryWrapper.eq("equipment_id",record.getEquipmentId()).eq("student_no",user.getStudentNo()).eq("status",2);
        EquipmentRecord equipmentRecord = equipmentRecordMapper.selectOne(equipmentRecordQueryWrapper);
        if(equipmentRecord!=null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_ERCORD_NOT_REPEAT);
        }

        Date now = new Date();
        EquipmentRecord newEquipmentRecord = new EquipmentRecord(
                null,
                record.getEquipmentId(),
                user.getStudentNo(),
                reason,
                record.getDeadlineTime(),
                now,
                null,
                null,
                2
        );
        equipmentRecordMapper.insert(newEquipmentRecord);
        //管理员自动审核通过
        if(user.getRole()==1){
            checkApplySerive.checkApplySerive(newEquipmentRecord.getId(),"pass","");
        }

        return Result.success(null);
    }
}
