package com.team.backend.service.impl.equipment.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.GetEquipmentInfoService;
import com.team.backend.service.equipment.record.GetApplyRecordService;
import com.team.backend.service.equipment.record.GetEquipmentRecordService;
import com.team.backend.utils.common.RecordShowType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GetEquipmentRecordServiceImpl implements GetEquipmentRecordService {
    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;
    @Autowired
    GetEquipmentInfoService getEquipmentInfoService;
    @Autowired
    UserMapper userMapper;

    @Override
    public Result getEquipmentRecord(int equipmentid) {
        QueryWrapper<EquipmentRecord> equipmentRecordQueryWrapper = new QueryWrapper<>();
        equipmentRecordQueryWrapper.eq("equipment_id",equipmentid).eq("status",2);
        List<EquipmentRecord> equipmentRecordList = equipmentRecordMapper.selectList(equipmentRecordQueryWrapper);
        Collections.reverse(equipmentRecordList);

        List<RecordShowType> recordShowTypes = new ArrayList<>();

        for (EquipmentRecord record:equipmentRecordList) {
            QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("student_no",record.getCheckNo());
            User checkInfo = userMapper.selectOne(queryWrapper2);

            QueryWrapper<User> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("student_no",record.getStudentNo());
            User applyInfo = userMapper.selectOne(queryWrapper3);

            RecordShowType recordShowType = new RecordShowType(
                    record.getId(),
                    getEquipmentInfoService.getEquipmentInfo(record.getEquipmentId()).getData(),
                    applyInfo,
                    record.getApplyReason(),
                    record.getDeadlineTime(),
                    record.getApplyTime(),
                    checkInfo,
                    record.getRefuseReason(),
                    record.getStatus()
            );
            recordShowTypes.add(recordShowType);
        }
        return Result.success(recordShowTypes);
    }
}
