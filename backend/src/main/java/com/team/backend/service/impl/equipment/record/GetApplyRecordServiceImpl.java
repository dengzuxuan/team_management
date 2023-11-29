package com.team.backend.service.impl.equipment.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.GetEquipmentInfoService;
import com.team.backend.service.equipment.record.GetApplyRecordService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.dto.resp.RecordShowType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GetApplyRecordServiceImpl implements GetApplyRecordService {
    @Autowired
    private GetEquipmentInfoService getEquipmentInfoService;

    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;

    @Autowired
    UserMapper userMapper;
    @Override
    public Result getApplyRecord() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<EquipmentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",user.getId());
        List<EquipmentRecord> equipmentRecordList = equipmentRecordMapper.selectList(queryWrapper);

        for (EquipmentRecord record:equipmentRecordList) {
            getEquipmentInfoService.updateEquipmentAndRecord(record.getEquipmentId());
        }

        List<EquipmentRecord> equipmentRecordList2 = equipmentRecordMapper.selectList(queryWrapper);
        //倒排
        Collections.reverse(equipmentRecordList2);

        List<RecordShowType> recordShowTypes = new ArrayList<>();

        for (EquipmentRecord record:equipmentRecordList2) {
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
            ).eq("id",record.getStudentId());
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
