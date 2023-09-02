package com.team.backend.service.impl.equipment.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.GetEquipmentInfoService;
import com.team.backend.service.equipment.record.GetApplyRecordService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class GetApplyRecordServiceImpl implements GetApplyRecordService {
    @Autowired
    private GetEquipmentInfoService getEquipmentInfoService;

    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;
    @Override
    public Result getApplyRecord() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<EquipmentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",user.getStudentNo());
        List<EquipmentRecord> equipmentRecordList = equipmentRecordMapper.selectList(queryWrapper);

        for (EquipmentRecord record:equipmentRecordList) {
            getEquipmentInfoService.updateEquipmentAndRecord(record.getEquipmentId());
        }

        List<EquipmentRecord> equipmentRecordList2 = equipmentRecordMapper.selectList(queryWrapper);
        //倒排
        Collections.reverse(equipmentRecordList2);

        return Result.success(equipmentRecordList2);
    }
}
